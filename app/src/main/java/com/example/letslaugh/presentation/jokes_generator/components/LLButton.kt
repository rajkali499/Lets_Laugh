package com.example.letslaugh.presentation.jokes_generator.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LLTextButton(btnOnClick: () -> Unit, buttonTxt: String, modifier: Modifier) {
    return Button(
        modifier = modifier,
        onClick = btnOnClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(0.5f)),
        contentPadding = PaddingValues(horizontal = 100.dp)
    ) {
        Text(text = buttonTxt)
    }
}