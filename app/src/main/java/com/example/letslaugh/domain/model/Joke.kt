package com.example.letslaugh.domain.model

import com.example.letslaugh.data.local.entity.JokeEntity

data class Joke(
    val id: Int? = null,
    val punchline: String ?= null,
    val setup: String ?= null,
    val isFavourite : Boolean ?= false
)

fun Joke.toJokeEntity(): JokeEntity {
    return JokeEntity(id = id?:0, punchline = punchline?:"", setup = setup?:"",type = "", isFavourite =  isFavourite)
}