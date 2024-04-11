package com.example.letslaugh.data.repository

import com.example.letslaugh.data.remote.JokesApi
import com.example.letslaugh.data.remote.dto.JokeDto
import com.example.letslaugh.domain.repository.JokeRepository
import javax.inject.Inject

class JokeRepositoryImpl @Inject constructor(
    private val api: JokesApi
) : JokeRepository {
    override suspend fun getJoke(): JokeDto {
        return api.getJoke()
    }

}