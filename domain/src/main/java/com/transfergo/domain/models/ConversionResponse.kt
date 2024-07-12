package com.transfergo.domain.models

data class ConversionResponse(
    val from: String,
    val to: String,
    val rate: Float,
    val fromAmount: Float,
    val toAmount: Float
)
