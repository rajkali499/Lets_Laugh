package com.example.letslaugh.domain.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.letslaugh.data.local.entity.JokeEntity

@Dao
interface JokeRoomDao {

    @Query("SELECT * FROM joke_entity")
    suspend fun getAllFavouriteJokes(): List<JokeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavouriteJokes(vararg jokesEntity: JokeEntity)

    @Query("DELETE FROM joke_entity WHERE id LIKE :id")
    suspend fun deleteFavJoke(id: Int)

    @Query("SELECT COUNT(*) FROM joke_entity WHERE id LIKE :id")
    suspend fun getFavouriteCount(id: Int): Int
}