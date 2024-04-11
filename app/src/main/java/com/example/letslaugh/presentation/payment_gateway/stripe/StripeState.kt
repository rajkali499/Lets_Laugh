package com.example.letslaugh.presentation.payment_gateway.stripe

import com.example.letslaugh.domain.model.StripeResponse

data class StripeState(
    val isLoading: Boolean = false,
    val result : StripeResponse = StripeResponse(),
    val error : String = "",
)
