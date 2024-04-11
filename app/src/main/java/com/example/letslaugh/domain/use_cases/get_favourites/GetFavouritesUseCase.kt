package com.example.letslaugh.domain.use_cases.get_favourites

import com.example.letslaugh.common.Resource
import com.example.letslaugh.data.local.entity.toJoke
import com.example.letslaugh.domain.model.Joke
import com.example.letslaugh.domain.repository.JokeRoomDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetFavouritesUseCase @Inject constructor(private val jokeRoomDao: JokeRoomDao) {
    operator fun invoke() : Flow<Resource<Joke>> = flow {
        try {
            emit(Resource.Loading<Joke>())
            val jokeEntityList = jokeRoomDao.getAllFavouriteJokes()
            val jokeList = jokeEntityList.map { it.toJoke() }.toList()
            emit(Resource.Success<Joke>(dataList = jokeList))
        }catch (e : HttpException){
            emit(Resource.Error<Joke>(message = e.localizedMessage?:"An Error occurred during process"))
        }catch (e : IOException) {
            emit(Resource.Error<Joke>(message = "Couldn't reach the server. Please check your Internet Connection"))
        }
    }
}