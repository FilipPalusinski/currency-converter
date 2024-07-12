package com.transfergo.data.models

import com.transfergo.domain.models.ConversionResponse

fun ConversionResponseDto.toConversionResponse(): ConversionResponse {
    return ConversionResponse(
        from = this.from,
        to = this.to,
        rate = this.rate,
        fromAmount = this.fromAmount,
        toAmount = this.toAmount
    )
}
