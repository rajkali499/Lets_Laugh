package com.example.letslaugh.presentation.feedback


import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.letslaugh.presentation.feedback.components.JokeRatingBar
import kotlin.random.Random

@Composable
fun FeedBackScreen() {

    var text by remember {
        mutableStateOf("0")
    }

    var rating by remember {
        mutableStateOf("0")
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val offsetState by animateIntOffsetAsState(
        targetValue = if (text.toFloat() != 5f ) {
            IntOffset(Random.nextInt((screenWidth.value * 0.05).toInt()), Random.nextInt((screenHeight.value * 0.5).toInt()))
        } else {
            IntOffset((screenWidth.value * 0.05).toInt(), (screenWidth.value * 0.3).toInt())
        }, label = "Offset for Animation"
    )
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        JokeRatingBar(
            rating = rating.toFloat(), modifier = Modifier.padding(10.dp), spaceBetween = 5.dp
        )
        TextField(
            modifier = Modifier.padding(10.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
            },
            shape = RoundedCornerShape(20.dp),
            placeholder = {
                Text(text = text)
            },
            value = text,
            colors = TextFieldDefaults.colors().copy(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            onValueChange = {
                text = it.ifEmpty { "0" }
            })
        ElevatedButton(
            interactionSource = interactionSource,
            modifier = Modifier
                .padding(10.dp)
                .offset(offsetState.x.dp, offsetState.y.dp),
            onClick = {
                if(text.toFloat() == 5f)
                    rating = text
            },
        ) {
            Text(text = "Ok")
        }
    }
}

