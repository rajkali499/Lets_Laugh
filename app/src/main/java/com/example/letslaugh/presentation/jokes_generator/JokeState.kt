package com.example.letslaugh.presentation.jokes_generator

import com.example.letslaugh.domain.model.Joke

data class JokeState(
    val isLoading: Boolean = false,
    val joke : Joke = Joke(),
    val error : String = "",
)
