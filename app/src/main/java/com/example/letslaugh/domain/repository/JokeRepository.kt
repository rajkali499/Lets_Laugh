package com.example.letslaugh.domain.repository

import com.example.letslaugh.data.remote.dto.JokeDto

interface JokeRepository {

    suspend fun getJoke() : JokeDto
}