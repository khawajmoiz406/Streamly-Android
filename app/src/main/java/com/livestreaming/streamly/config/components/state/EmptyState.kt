package com.livestreaming.streamly.config.components.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.livestreaming.streamly.config.components.image.SvgImage
import ir.kaaveh.sdpcompose.sdp

@Composable
fun EmptyState(message: String, scrollable: Boolean = true, imageAsset: String = "") {
    val modifier = Modifier.fillMaxSize()
    if (scrollable) modifier.verticalScroll(rememberScrollState())

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
        content = {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                if (imageAsset.isNotEmpty()) {
                    SvgImage(
                        asset = imageAsset,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(150.sdp)
                    )
                }

                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    )
}