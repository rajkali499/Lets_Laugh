package com.example.letslaugh.presentation.jokes_generator.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.letslaugh.R
import com.example.letslaugh.presentation.jokes_generator.GetJokeViewModel
import com.example.letslaugh.presentation.jokes_generator.JokeState
import com.example.letslaugh.presentation.ui.theme.onPrimaryLight

@Composable
fun JokeItem(modifier: Modifier) {
    val viewModel = hiltViewModel<GetJokeViewModel>()

    LaunchedEffect(key1 = Unit) {
        viewModel.getJoke()
    }

    JokeCard(viewModel, viewModel.state, viewModel.isFavourite, modifier)

}


@Composable
fun JokeCard(
    vm: GetJokeViewModel,
    state: State<JokeState>,
    isFavourite: State<Boolean>,
    modifier: Modifier
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val offsetState by animateIntOffsetAsState(
        targetValue = if (state.value.joke.setup?.isNotEmpty() == true) {
            IntOffset(((screenWidth.value * 0.25).toInt()), ((screenHeight.value * 0.15).toInt()))
        } else {
            IntOffset((screenWidth.value * 0.25).toInt(), (screenWidth.value * 0.65).toInt())
        }, label = "Offset for Animation"
    )
    Box {
        Image(
            modifier = modifier
                .size(200.dp)
                .offset(offsetState.x.dp, offsetState.y.dp),
            painter = painterResource(id = R.drawable.wink_emoji),
            contentDescription = "Wink Emoji"
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 50.dp)
                    .weight(1f, false),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.24f))
                ) {
                    if (state.value.isLoading) {
                        CircularProgressIndicator(
                            modifier.padding(
                                top = 150.dp,
                                bottom = 10.dp,
                                start = 100.dp,
                                end = 100.dp
                            ), color = onPrimaryLight
                        )
                    } else
                        if (state.value.error.isNotEmpty()) {
                            Text(text = state.value.error, style = TextStyle(color = Color.Red))
                        } else {
                            CardData(
                                vm = vm,
                                modifier = modifier,
                                state = state,
                                isFavourite = isFavourite
                            )
                        }
                }
            }
            LLTextButton(
                btnOnClick = {
                    vm.getJoke()
                },
                buttonTxt = "Next",
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardData(
    vm: GetJokeViewModel,
    modifier: Modifier, state: State<JokeState>,
    isFavourite: State<Boolean>,
) {
    var showAnswer by remember {
        mutableStateOf(false)
    }
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
            state.value.joke.setup?.let {
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
                    if (isFavourite.value) {
                        state.value.joke.id?.let { vm.deleteFavourite(it) }
                    } else {
                        vm.addToFavourites(state.value.joke)
                    }
                }) {
                    Icon(
                        painter = painterResource(id = if (isFavourite.value) R.drawable.favorite_filled else R.drawable.favorite_outlined),
                        contentDescription = "Fav",
                        tint = if (isFavourite.value) Color.Red else Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .fillMaxWidth(0.2f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        AnimatedVisibility(visible = showAnswer) {
            state.value.joke.punchline?.let {
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

