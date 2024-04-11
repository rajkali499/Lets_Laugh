package com.example.letslaugh.data.repository

import com.example.letslaugh.data.local.database.JokesDatabase
import com.example.letslaugh.data.local.entity.JokeEntity
import com.example.letslaugh.domain.repository.JokeRoomDao
import javax.inject.Inject

class JokeRoomDatabaseImpl @Inject constructor(
    private val database: JokesDatabase
) : JokeRoomDao {
    override suspend fun getAllFavouriteJokes(): List<JokeEntity> {
        return database.jokeRoomDao().getAllFavouriteJokes()
    }

    override suspend fun addToFavouriteJokes(vararg jokesEntity: JokeEntity) {
        database.jokeRoomDao().addToFavouriteJokes(jokesEntity = jokesEntity)
    }

    override suspend fun deleteFavJoke(id: Int) {
        database.jokeRoomDao().deleteFavJoke(id)
    }

    override suspend fun getFavouriteCount(id: Int): Int {
        val result: Int = database.jokeRoomDao().getFavouriteCount(id)
        return result
    }
}