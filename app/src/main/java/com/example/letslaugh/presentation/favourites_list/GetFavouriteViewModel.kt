package com.example.letslaugh.presentation.favourites_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letslaugh.common.Resource
import com.example.letslaugh.domain.repository.JokeRoomDao
import com.example.letslaugh.domain.use_cases.get_favourites.GetFavouritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetFavouriteViewModel @Inject constructor(private val getFavouritesUseCase: GetFavouritesUseCase,private  val jokeRoomDao: JokeRoomDao) :
    ViewModel() {
    private val _favouriteState = MutableStateFlow(FavouriteJokeState())
    val favouriteState: StateFlow<FavouriteJokeState> = _favouriteState.asStateFlow()

    fun getFavouriteJokeList() {
        getFavouritesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _favouriteState.value = result.dataList?.let { FavouriteJokeState(jokeList = it) }!!
                    Log.d("GetFavourites","Success ${result.dataList.size}")
                }

                is Resource.Loading -> {
                    _favouriteState.value = FavouriteJokeState(isLoading = true)
                }

                is Resource.Error -> {
                    _favouriteState.value =
                        FavouriteJokeState(error = result.message ?: "An Unexpected Error occurred")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteFavourite(jokeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            jokeRoomDao.deleteFavJoke(jokeId)
            Log.d("Delete","Success")
        }
    }
}