package com.livestreaming.streamly.config.components.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

/**
 * Composable function to display an SVG image from the assets folder.
 *
 * @param asset The filename of the SVG file in the assets folder (e.g., "splash_union" no need for extension just name)
 * @param modifier Modifier to be applied to the image
 * @param color Optional tint color to apply to the SVG image
 */
@Composable
fun SvgImage(asset: String, modifier: Modifier = Modifier, color: Color? = null) {
    AsyncImage(
        contentDescription = null,
        modifier = modifier,
        colorFilter = color?.let { ColorFilter.tint(color = it) },
        model = ImageRequest.Builder(LocalContext.current)
            .data("file:///android_asset/icons/${asset}.svg")
            .decoderFactory(SvgDecoder.Factory())
            .build(),
    )
}