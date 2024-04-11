package com.example.letslaugh.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.letslaugh.domain.model.Joke

@Entity(tableName = "joke_entity")
data class JokeEntity(
   @PrimaryKey val id: Int,
   @ColumnInfo val punchline: String,
   @ColumnInfo val setup: String,
   @ColumnInfo val type: String,
   @ColumnInfo val isFavourite: Boolean ? = false
)

fun JokeEntity.toJoke() : Joke {
   return Joke(id = id, punchline = punchline, setup = setup, isFavourite = isFavourite)
}


