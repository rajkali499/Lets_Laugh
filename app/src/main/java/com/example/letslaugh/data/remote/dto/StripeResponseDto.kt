package com.example.letslaugh.data.remote.dto

import com.example.letslaugh.domain.model.StripeResponse

data class StripeResponseDto(
   val publishableKey: String, // https://stripe.com/docs/keys#obtain-api-keys
   val paymentIntent: String,
   val customer: String,
   val ephemeralKey: String
)

fun StripeResponseDto.toStripeResponse() : StripeResponse {
   return StripeResponse(publishableKey,paymentIntent,customer,ephemeralKey)
}
