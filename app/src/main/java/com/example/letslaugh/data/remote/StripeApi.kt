package com.example.letslaugh.data.remote

import com.example.letslaugh.data.remote.dto.StripeResponseDto
import retrofit2.http.POST

interface StripeApi {

    @POST("/checkout")
    suspend fun checkout() : StripeResponseDto

}