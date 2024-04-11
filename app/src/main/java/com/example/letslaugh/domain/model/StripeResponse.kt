package com.example.letslaugh.domain.model

data class StripeResponse(
    val publishableKey: String?="", // https://stripe.com/docs/keys#obtain-api-keys
    val paymentIntent: String?="",
    val customer: String?="",
    val ephemeralKey: String?=""
)
