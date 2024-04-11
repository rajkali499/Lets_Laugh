package com.example.letslaugh.presentation.favourites_list

import com.example.letslaugh.domain.model.Joke

data class FavouriteJokeState(
    val isLoading: Boolean = false,
    val jokeList : List<Joke> = listOf(),
    val error : String = "",
)
