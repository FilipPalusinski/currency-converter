package com.transfergo.data

import com.transfergo.common.Resource
import com.transfergo.data.models.ConversionResponseDto
import com.transfergo.data.models.toConversionResponse
import com.transfergo.data.remote.ApiService
import com.transfergo.data.repository.ConversionRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class ConversionRepositoryImplTest {

    private val apiService: ApiService = mockk()

    private lateinit var tested: ConversionRepositoryImpl

    @Before
    fun setup() {
        tested = ConversionRepositoryImpl(apiService)
    }

    @Test
    fun `convertCurrency success returns Resource Success`() = runTest {
        // Arrange
        val mockResponse = ConversionResponseDto("PLN", "UAH", 10.44575f, 100f, 1044.58f)
        coEvery { apiService.convertCurrency("USD", "EUR", 100f) } returns Response.success(mockResponse)

        // Act
        val result = tested.convertCurrency("USD", "EUR", 100f)

        // Assert
        assert(result is Resource.Success && result.data == mockResponse.toConversionResponse())
    }

    @Test
    fun `convertCurrency failure returns Resource Error`() = runTest {
        // Arrange
        coEvery { apiService.convertCurrency("USD", "EUR", 100f) } returns Response.error(404, okhttp3.ResponseBody.create(null, "Not found"))

        // Act
        val result = tested.convertCurrency("USD", "EUR", 100f)

        // Assert
        assert(result is Resource.Error)
    }

    @Test
    fun `convertCurrency IOException returns Resource Error for network issues`() = runTest {
        // Arrange
        coEvery { apiService.convertCurrency("USD", "EUR", 100f) } throws IOException("Network failure")

        // Act
        val result = tested.convertCurrency("USD", "EUR", 100f)

        // Assert
        assert(result is Resource.Error)
        assertEquals("Check your internet connection java.io.IOException: Network failure", (result as Resource.Error).message)
    }

    @Test
    fun `convertCurrency HttpException returns Resource Error with message`() = runTest {
        // Arrange
        val httpException = retrofit2.HttpException(Response.error<Any>(500, okhttp3.ResponseBody.create(null, "Internal server error")))
        coEvery { apiService.convertCurrency("USD", "EUR", 100f) } throws httpException

        // Act
        val result = tested.convertCurrency("USD", "EUR", 100f)

        // Assert
        assert(result is Resource.Error)
    }

}
