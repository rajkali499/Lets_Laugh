package com.example.letslaugh.data.remote.dto

import com.example.letslaugh.data.local.entity.JokeEntity
import com.example.letslaugh.domain.model.Joke

data class JokeDto(
    val id: Int,
    val punchline: String,
    val setup: String,
    val type: String
)

fun JokeDto.toJoke(): Joke {
    return Joke(id = id, punchline = punchline, setup = setup)
}

fun JokeDto.toJokeEntity(): JokeEntity {
    return JokeEntity(id = id, punchline = punchline, setup = setup,type = type)
}
