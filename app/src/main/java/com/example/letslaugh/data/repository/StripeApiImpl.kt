package com.example.letslaugh.data.repository

import com.example.letslaugh.data.remote.StripeApi
import com.example.letslaugh.data.remote.dto.StripeResponseDto
import com.example.letslaugh.domain.repository.StripeRepository
import javax.inject.Inject

class StripeApiImpl @Inject constructor(private val stripeApi: StripeApi) : StripeRepository {
    override suspend fun checkout() : StripeResponseDto {
        return stripeApi.checkout()
    }

}