package com.transfergo.data.models

import com.squareup.moshi.Json

data class ConversionResponseDto(
    @Json(name = "from") val from: String,
    @Json(name = "to") val to: String,
    @Json(name = "rate") val rate: Float,
    @Json(name = "fromAmount") val fromAmount: Float,
    @Json(name = "toAmount") val toAmount: Float
)
