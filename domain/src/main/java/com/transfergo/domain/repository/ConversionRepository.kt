package com.transfergo.domain.repository

import com.transfergo.common.Resource
import com.transfergo.domain.models.ConversionResponse

interface ConversionRepository {

    suspend fun convertCurrency(from: String, to: String, amount: Float): Resource<ConversionResponse>

}
