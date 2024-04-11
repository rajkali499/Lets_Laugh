package com.example.letslaugh.domain.repository

import com.example.letslaugh.data.remote.dto.StripeResponseDto

interface StripeRepository {

  suspend  fun checkout() : StripeResponseDto
}