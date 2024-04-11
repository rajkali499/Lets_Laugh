package com.example.letslaugh.presentation.ai

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.letslaugh.presentation.ai.components.FancyAnimatedIndicatorWithModifier
import com.example.letslaugh.presentation.ai.components.FancyTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIScreen() {
    var state by remember { mutableIntStateOf(0) }
    val titles = listOf("Face Detection", "Scanner", "Tab 3", "Tab 4", "Tab 5", "Tab 6")
    Column {
        ScrollableTabRow(
            edgePadding = 0.dp,
            indicator = {
                FancyAnimatedIndicatorWithModifier(state, it)
            },
            selectedTabIndex = state
        ) {
            titles.forEachIndexed { index, title ->
                FancyTab(
                    title = title,
                    onClick = { state = index },
                    selected = (index == state)
                )
            }
        }
        AIContentTab(tab = state)
    }
}

@Composable
fun AIContentTab(tab: Int) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.VIDEO_CAPTURE
            )
        }
    }

    when (tab) {
        0 -> FaceDetectionScreen(
            controller, modifier = Modifier
                .fillMaxSize()
        )

        else -> Text(
            color = Color.White,
            text = "Fancy tab ${tab + 1} selected",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}



