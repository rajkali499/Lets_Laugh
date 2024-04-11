package com.example.letslaugh.presentation.jokes_generator


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letslaugh.common.Resource
import com.example.letslaugh.domain.model.Joke
import com.example.letslaugh.domain.model.toJokeEntity
import com.example.letslaugh.domain.repository.JokeRoomDao
import com.example.letslaugh.domain.use_cases.get_joke.GetJokeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetJokeViewModel @Inject constructor(
    private val getJokeUseCase: GetJokeUseCase,
    private val jokeRoomDao: JokeRoomDao
) : ViewModel() {

    private val _state = mutableStateOf(JokeState())
    val state: State<JokeState> = _state
    private var _isFavourite = mutableStateOf(false)
    val isFavourite: State<Boolean> = _isFavourite

    init {
        getJoke()
    }

    fun getJoke() {
        getJokeUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = result.data?.let { JokeState(joke = it) }!!
                    viewModelScope.launch(Dispatchers.IO) {
                        val count = result.data.id?.let { jokeRoomDao.getFavouriteCount(it) }
                        _isFavourite.value = (count ?: 0) > 0
                    }
                }

                is Resource.Loading -> {
                    _state.value = JokeState(isLoading = true)
                }

                is Resource.Error -> {
                    _state.value =
                        JokeState(error = result.message ?: "An Unexpected Error occurred")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addToFavourites(joke: Joke) {
        viewModelScope.launch(Dispatchers.IO) {
            val jokeUpdated = joke.copy(isFavourite = true)
            jokeRoomDao.addToFavouriteJokes(jokeUpdated.toJokeEntity())
            _isFavourite.value = true
        }
    }

    fun deleteFavourite(jokeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            jokeRoomDao.deleteFavJoke(jokeId)
            _isFavourite.value = false
        }
    }
}