package com.transfergo.data.remote

import com.transfergo.data.models.ConversionResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("fx-rates")
    suspend fun convertCurrency( @Query("from") fromCurrency: String,
                                 @Query("to") toCurrency: String,
                                 @Query("amount") amount: Float): Response<ConversionResponseDto>

    companion object {
        const val BASE_URL = "https://my.transfergo.com/api/"
    }
}
