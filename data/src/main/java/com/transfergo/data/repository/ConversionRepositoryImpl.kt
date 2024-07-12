package com.transfergo.data.repository

import com.transfergo.common.Resource
import com.transfergo.data.models.toConversionResponse
import com.transfergo.data.remote.ApiService
import com.transfergo.domain.models.ConversionResponse
import com.transfergo.domain.repository.ConversionRepository
import java.io.IOException
import javax.inject.Inject

class ConversionRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ConversionRepository {
    override suspend fun convertCurrency(from: String, to: String, amount: Float): Resource<ConversionResponse> {
        return try {
            val response = apiService.convertCurrency(from, to, amount)
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it.toConversionResponse())
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error(response.toString())
            }
        } catch (e: IOException) {
            Resource.Error("Check your internet connection $e")
        } catch (e: retrofit2.HttpException) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occured")
        }
    }


}
