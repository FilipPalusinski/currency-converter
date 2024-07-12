package com.transfergo.data.models

import com.squareup.moshi.Json

data class ConversionRequestDto(
    @Json(name = "from") val from: String,
    @Json(name = "to") val to: String,
    @Json(name = "amount") val amount: Float
)
