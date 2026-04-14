package com.livestreaming.streamly.config.components.state

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shimmerColor: Color = Color.LightGray.copy(alpha = 0.6f),
    shimmerHighlightColor: Color = Color.White.copy(alpha = 0.3f),
    shimmerDuration: Int = 1200
) {
    val shimmerColors = listOf(shimmerColor, shimmerHighlightColor, shimmerColor)
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        label = "translateAnim",
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = shimmerDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, translateAnim),
        end = Offset(translateAnim + 200f, translateAnim + 200f)
    )

    Box(modifier = modifier.background(brush))
}
