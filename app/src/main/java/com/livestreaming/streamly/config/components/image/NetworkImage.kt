package com.livestreaming.streamly.config.components.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.livestreaming.streamly.config.components.state.ShimmerBox

@Composable
fun NetworkImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
    animateOnLoad: Boolean = true,
    showShimmerWhenLoading: Boolean = true,
) {
    var isLoading by remember { mutableStateOf(true) }

    Box(modifier = modifier) {
        if (isLoading && showShimmerWhenLoading) {
            ShimmerBox(modifier = modifier.fillMaxSize())
        }

        AsyncImage(
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier,
            onSuccess = { isLoading = false },
            onError = { isLoading = false },
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(animateOnLoad)
                .crossfade(500)
                .build(),
        )
    }
}