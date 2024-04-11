package com.example.letslaugh.domain.use_cases.get_joke

import com.example.letslaugh.common.Resource
import com.example.letslaugh.data.remote.dto.toJoke
import com.example.letslaugh.domain.model.Joke
import com.example.letslaugh.domain.repository.JokeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetJokeUseCase @Inject constructor(
    private val repository: JokeRepository
) {
    operator fun invoke() : Flow<Resource<Joke>> = flow {
        try {
            emit(Resource.Loading<Joke>())
            val joke = repository.getJoke().toJoke()
            emit(Resource.Success<Joke>(data = joke))
        }catch (e : HttpException){
            emit(Resource.Error<Joke>(message = e.localizedMessage?:"An Error occurred during process"))
        }catch (e : IOException) {
            emit(Resource.Error<Joke>(message = "Couldn't reach the server. Please check your Internet Connection"))
        }
    }
}