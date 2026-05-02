package com.livestreaming.streamly.ui.watch.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.components.layout.UserImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.core.model.StreamStatus
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun BoxScope.WatchTopOverlay(
    title: String,
    hostName: String,
    hostPhoto: String?,
    status: StreamStatus?,
    viewers: Int,
    muted: Boolean,
    onBackPressed: () -> Unit,
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val shape = RoundedCornerShape(20.sdp)
    val cardColors = Color.Black.copy(alpha = 0.7f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.align(Alignment.TopCenter)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Black.copy(alpha = 0.7f),
                            0.6f to Color.Black.copy(alpha = 0.3f),
                            0.8f to Color.Black.copy(alpha = 0.1f),
                            1.0f to Color.Transparent,
                        )
                    )
                )
                .padding(10.sdp)
        ) {
            Spacer(Modifier.height(statusBarHeight))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(28.sdp)
                        .background(cardColors, CircleShape)
                        .clip(CircleShape)
                        .clickable { onBackPressed.invoke() }
                ) {
                    SvgImage(
                        asset = "back",
                        color = Color.White,
                        modifier = Modifier.size(18.sdp)
                    )
                }

                Spacer(Modifier.weight(1f))

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
            }

            Spacer(Modifier.height(15.sdp))

            HostInfo(title, hostName, hostPhoto)
        }

        if (muted) {
            Spacer(Modifier.height(20.sdp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.errorContainer, shape)
                    .padding(horizontal = 12.sdp, vertical = 4.sdp)
            ) {
                SvgImage(
                    asset = "micro_phone_disable",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(15.sdp)
                )

                Spacer(Modifier.width(8.sdp))

                Text(
                    text = stringResource(R.string.host_mic_muted),
                    fontSize = 11.ssp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    }
}

@Composable
private fun HostInfo(title: String, hostName: String, hostPhoto: String?) {
    Row {
        UserImage(userName = hostName, profilePicture = hostPhoto, size = 28.sdp)

        Spacer(Modifier.width(10.sdp))

        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(
                text = hostName,
                color = Color.White,
                fontSize = 12.ssp,
                lineHeight = 12.ssp,
            )

            Text(
                text = title,
                fontSize = 10.ssp,
                lineHeight = 10.ssp,
                color = MaterialTheme.colorScheme.disabledContent,
            )
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
private fun PreviewWatchTopOverlay() {
    MyApplicationTheme {
        Box {
            WatchTopOverlay(
                "This is my first stream",
                "Super Admin",
                null,
                StreamStatus.Live,
                100,
                true
            ) {}
        }
    }
}