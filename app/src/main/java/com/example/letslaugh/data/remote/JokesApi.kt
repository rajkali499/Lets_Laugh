package com.example.letslaugh.data.remote

import com.example.letslaugh.data.remote.dto.JokeDto
import retrofit2.http.GET

interface JokesApi {
  @GET("/random_joke")
  suspend fun  getJoke() : JokeDto
}