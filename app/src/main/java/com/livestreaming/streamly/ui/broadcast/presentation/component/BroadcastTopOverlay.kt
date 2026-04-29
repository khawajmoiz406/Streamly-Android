package com.livestreaming.streamly.ui.broadcast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.core.model.StreamStatus
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun BoxScope.BroadcastTopOverlay(status: StreamStatus?, viewers: Int) {
    val shape = RoundedCornerShape(20.sdp)
    val cardColors = Color.Black.copy(alpha = 0.7f)

    Row(
        Modifier
            .background(Color.Transparent)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(10.sdp)
            .align(Alignment.TopCenter)
    ) {
        status?.let {
            val buttonInfo = getButtonInfoAccordingly(it, MaterialTheme.colorScheme)

            Row(
                Modifier
                    .background(buttonInfo["color"] as Color, shape)
                    .padding(horizontal = 12.sdp, vertical = 3.sdp)
            ) {
                Text(
                    text = stringResource(buttonInfo["label"] as Int).uppercase(),
                    fontSize = 10.ssp,
                    fontWeight = FontWeight.Medium,
                    color = buttonInfo["labelColor"] as Color
                )
            }

            Spacer(Modifier.width(10.sdp))
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(cardColors, shape)
                .padding(horizontal = 12.sdp, vertical = 3.sdp)
        ) {
            SvgImage(
                asset = "eye_open",
                color = Color.White,
                modifier = Modifier.size(12.sdp)
            )

            Spacer(Modifier.width(5.sdp))

            Text(
                text = viewers.toString(),
                fontSize = 10.ssp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
            )
        }

        Spacer(Modifier.weight(1f))

        StreamTimer(
            status = status,
            modifier = Modifier
                .background(cardColors, shape)
                .padding(horizontal = 12.sdp, vertical = 3.sdp)
        )
    }
}

private fun getButtonInfoAccordingly(streamStatus: StreamStatus, colorScheme: ColorScheme) =
    when (streamStatus) {
        StreamStatus.Setting -> mapOf(
            "label" to R.string.setting_things_up,
            "color" to Color.Black.copy(alpha = 0.7f),
            "labelColor" to Color.White
        )

        StreamStatus.Paused -> mapOf(
            "label" to R.string.paused,
            "color" to colorScheme.secondary,
            "labelColor" to colorScheme.onSecondary
        )

        StreamStatus.Live -> mapOf(
            "label" to R.string.live,
            "color" to colorScheme.errorContainer,
            "labelColor" to colorScheme.onErrorContainer
        )

        StreamStatus.Ended -> mapOf(
            "label" to R.string.ended,
            "color" to colorScheme.primary,
            "labelColor" to colorScheme.onPrimary
        )
    }

@Preview(device = Devices.PIXEL_7)
@Composable
private fun PreviewBroadcastTopOverlay() {
    MyApplicationTheme {
        Box {
            BroadcastTopOverlay(StreamStatus.Live, 100)
        }
    }
}