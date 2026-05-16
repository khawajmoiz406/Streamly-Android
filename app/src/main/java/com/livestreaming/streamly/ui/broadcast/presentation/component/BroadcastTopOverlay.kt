package com.livestreaming.streamly.ui.broadcast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
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
fun BoxScope.BroadcastTopOverlay(
    status: StreamStatus?,
    viewers: Int,
    muted: Boolean,
    isInPipMode: Boolean
) {
    val shape = RoundedCornerShape(20.sdp)
    val cardColors = Color.Black.copy(alpha = 0.7f)

    val textSize = if (isInPipMode) 7.ssp else 10.ssp
    val space = if (isInPipMode) 5.sdp else 10.sdp
    val paddingHorizontal = if (isInPipMode) 6.sdp else 12.sdp
    val paddingVertical = if (isInPipMode) 4.sdp else 7.sdp
    val imageSize = if (isInPipMode) 7.sdp else 12.sdp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.Transparent)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(if (isInPipMode) 5.sdp else 10.sdp)
            .align(Alignment.TopCenter)
    ) {
        Row {
            status?.let {
                val buttonInfo = getButtonInfoAccordingly(it, MaterialTheme.colorScheme)

                Row(
                    Modifier
                        .background(buttonInfo["color"] as Color, shape)
                        .padding(horizontal = paddingHorizontal, vertical = paddingVertical)
                ) {
                    Text(
                        text = stringResource(buttonInfo["label"] as Int).uppercase(),
                        fontSize = textSize,
                        lineHeight = textSize,
                        fontWeight = FontWeight.Medium,
                        color = buttonInfo["labelColor"] as Color
                    )
                }

                Spacer(Modifier.width(space))
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(cardColors, shape)
                    .padding(horizontal = paddingHorizontal, vertical = paddingVertical)
            ) {
                SvgImage(
                    asset = "eye_open",
                    color = Color.White,
                    modifier = Modifier.size(imageSize)
                )

                Spacer(Modifier.width(5.sdp))

                Text(
                    text = viewers.toString(),
                    fontSize = textSize,
                    lineHeight = textSize,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                )
            }

            Spacer(Modifier.weight(1f))

            StreamTimer(
                status = status,
                textSize = textSize,
                modifier = Modifier
                    .background(cardColors, shape)
                    .padding(horizontal = paddingHorizontal, vertical = paddingVertical)
            )
        }

        if (muted) {
            Spacer(Modifier.height(if (isInPipMode) 10.sdp else 20.sdp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.errorContainer, shape)
                    .padding(horizontal = paddingHorizontal, vertical = paddingVertical)
            ) {
                SvgImage(
                    asset = "micro_phone_disable",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(if (isInPipMode) 8.sdp else 15.sdp)
                )

                Spacer(Modifier.width(8.sdp))

                Text(
                    text = stringResource(R.string.mic_muted),
                    fontSize = if (isInPipMode) 7.ssp else 11.ssp,
                    lineHeight = if (isInPipMode) 7.ssp else 11.ssp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }

    }
}

private fun getButtonInfoAccordingly(streamStatus: StreamStatus, colorScheme: ColorScheme) =
    when (streamStatus) {
        StreamStatus.Setting -> mapOf(
            "label" to R.string.setting_things_up,
            "color" to Color.Black.copy(alpha = 0.7f),
            "labelColor" to Color.White
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
            BroadcastTopOverlay(StreamStatus.Live, 100, muted = true, isInPipMode = false)
        }
    }
}