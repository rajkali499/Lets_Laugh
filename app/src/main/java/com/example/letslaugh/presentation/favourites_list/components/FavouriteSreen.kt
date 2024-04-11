package com.example.letslaugh.presentation.favourites_list.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.letslaugh.R
import com.example.letslaugh.domain.model.Joke
import com.example.letslaugh.presentation.favourites_list.GetFavouriteViewModel
import com.example.letslaugh.presentation.jokes_generator.GetJokeViewModel
import com.example.letslaugh.presentation.ui.theme.onPrimaryLight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun JokeFavouriteList() {
    val viewModel = hiltViewModel<GetFavouriteViewModel>()
    val state by viewModel.favouriteState.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        viewModel.getFavouriteJokeList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                Modifier.padding(
                    top = 150.dp,
                    bottom = 10.dp,
                    start = 100.dp,
                    end = 100.dp
                ), color = onPrimaryLight
            )
        } else
            if (state.error.isNotEmpty()) {
                Text(text = state.error, style = TextStyle(color = Color.Red))
            } else {
                LazyColumn(contentPadding = PaddingValues(10.dp)) {
                    items(state.jokeList) {
                        CardData(item = it, modifier = Modifier.padding(5.dp), onDelete = { id ->
                            scope.launch(Dispatchers.IO) {
                                viewModel.deleteFavourite(id)
                                delay(100)
                                viewModel.getFavouriteJokeList()
                            }
                        })
                    }
                }
            }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardData(
    item: Joke,
    modifier: Modifier,
    onDelete: (id: Int) -> Unit,
) {
    val viewModel = hiltViewModel<GetJokeViewModel>()
    var showAnswer by remember {
        mutableStateOf(false)
    }
    val isFavourite by remember {
        mutableStateOf(item.isFavourite)
    }

    Card(
        modifier = Modifier.padding(20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.24f))
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                item.setup?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        text = it,
                        color = Color.White
                    )
                }
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text("Like")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    IconButton(onClick = {
                            item.id?.let { itemId ->
                                onDelete.invoke(itemId)
                            }
                    }) {
                        Icon(
                            painter = painterResource(id = if (isFavourite == true) R.drawable.favorite_filled else R.drawable.favorite_outlined),
                            contentDescription = "Fav",
                            tint = if (isFavourite == true) Color.Red else Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .fillMaxWidth(0.2f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            AnimatedVisibility(visible = showAnswer) {
                item.punchline?.let {
                    Card(
                        colors = CardDefaults.cardColors(
                            Color.Black, onPrimaryLight,
                            Color.Transparent, Color.Transparent
                        )
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(8.dp),
                            text = it,
                            color = Color.White
                        )
                    }
                }
            }
            Row(
                modifier = modifier
                    .padding(8.dp, 0.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text("Answer")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    IconButton(onClick = {
                        showAnswer = !showAnswer
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.answer),
                            contentDescription = "Ans",
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp, 20.dp)
                        )
                    }
                }
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text("Copy")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    IconButton(onClick = {
                        //                                    vm.getJoke()
                        //                                    showAnswer = false
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.copy),
                            contentDescription = "WinkEmoji",
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp, 20.dp)
                        )
                    }
                }
            }
        }
    }
}