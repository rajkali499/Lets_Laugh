package com.example.letslaugh.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.letslaugh.data.local.entity.JokeEntity
import com.example.letslaugh.domain.repository.JokeRoomDao

@Database(entities = [JokeEntity::class], version = 1, exportSchema = false)
abstract class JokesDatabase : RoomDatabase() {
    abstract fun jokeRoomDao(): JokeRoomDao

    companion object {
        const val DATABASE_NAME = "JokeRoomDB.db"
    }
}