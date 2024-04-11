package com.example.letslaugh.presentation.feedback.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getDrawable
import com.example.letslaugh.R


@Composable
fun JokeRatingBar(
    modifier: Modifier,
    rating: Float,
    spaceBetween: Dp = 0.dp
) {

    val ctx = LocalContext.current
    val image = getDrawable(ctx,R.drawable.star)?.let { createBitmap(drawable = it) }
//    val image = ImageBitmap.imageResource(id = R.drawable.star)
//    val imageFull = ImageBitmap.imageResource(id = R.drawable.star_full)
    val imageFull = getDrawable(ctx,R.drawable.star_full)?.let { createBitmap(drawable = it) }

    val totalCount = 5

    val height = LocalDensity.current.run { image?.height?.toDp() }
    val width = LocalDensity.current.run { image?.width?.toDp() }
    val space = LocalDensity.current.run { spaceBetween.toPx() }
    val totalWidth = (width?: 0.dp) * totalCount + spaceBetween * (totalCount - 1)


    height?.let {
        modifier
            .width(totalWidth)
            .height(it)
            .clickable {

            }
            .drawBehind {
                image?.asImageBitmap()?.let { it1 -> imageFull?.asImageBitmap()
                    ?.let { it2 -> drawRating(rating, it1, it2, space) } }
            }
    }?.let {
        Box(
            it
        )
    }
}

private fun DrawScope.drawRating(
    rating: Float,
    image: ImageBitmap,
    imageFull: ImageBitmap,
    space: Float
) {

    val totalCount = 5

    val imageWidth = image?.width?.toFloat() ?: 0.00f
    val imageHeight = size.height

    val reminder = rating - rating.toInt()
    val ratingInt = (rating - reminder).toInt()

    for (i in 0 until totalCount) {

        val start = imageWidth * i + space * i

        drawImage(
            image = image,
            topLeft = Offset(start, 0f)
        )
    }

    drawWithLayer {
        for (i in 0 until totalCount) {
            val start = imageWidth * i + space * i
            // Destination
            drawImage(
                image = imageFull,
                topLeft = Offset(start, 0f)
            )
        }

        val end = imageWidth * totalCount + space * (totalCount - 1)
        val start = rating * imageWidth + ratingInt * space
        val size = end - start

        // Source
        drawRect(
            Color.Transparent,
            topLeft = Offset(start, 0f),
            size = Size(size, height = imageHeight),
            blendMode = BlendMode.SrcIn
        )
    }
}


private fun DrawScope.drawWithLayer(block: DrawScope.() -> Unit) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        block()
        restoreToCount(checkPoint)
    }
}

fun createBitmap(drawable: Drawable): Bitmap? {
    try {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas: Canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    } catch (e: OutOfMemoryError) {
        // Handle the error
        return null
    }
}

@Preview
@Composable
fun Preview(modifier: Modifier = Modifier) {
    JokeRatingBar(
        rating = 5f,
        modifier = Modifier.padding(10.dp),
        spaceBetween = 5.dp
    )
}