package com.example.letslaugh.domain.use_cases.add_to_favourite

import com.example.letslaugh.data.local.entity.JokeEntity
import com.example.letslaugh.domain.repository.JokeRoomDao
import javax.inject.Inject

class AddToFavouriteUseCase @Inject constructor(private val jokeRoomDao: JokeRoomDao) {
   suspend operator fun invoke(vararg jokesEntity: JokeEntity){
       jokeRoomDao.addToFavouriteJokes(*jokesEntity)
   }
}