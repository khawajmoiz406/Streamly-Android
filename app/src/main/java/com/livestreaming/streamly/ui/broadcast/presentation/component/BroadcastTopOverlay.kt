package com.livestreaming.streamly.ui.broadcast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContainer
import com.livestreaming.streamly.core.model.StreamStatus
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun BoxScope.BroadcastTopOverlay(status: StreamStatus?, viewers: Int) {
    val shape = RoundedCornerShape(20.sdp)
    val cardColors = MaterialTheme.colorScheme.disabledContainer.copy(alpha = 0.5f)
    val buttonInfo = getButtonInfoAccordingly(status, MaterialTheme.colorScheme)

    Row(
        Modifier
            .background(Color.Transparent)
            .padding(10.sdp)
            .align(Alignment.TopCenter)
    ) {
        Row(
            Modifier
                .background(buttonInfo["color"] as Color, shape)
                .padding(horizontal = 8.sdp)
        ) {
            Text(
                text = stringResource(buttonInfo["label"] as Int).uppercase(),
                fontSize = 9.ssp,
                color = buttonInfo["labelColor"] as Color
            )
        }

        Spacer(Modifier.width(10.sdp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(cardColors, shape)
                .padding(horizontal = 5.sdp)
        ) {
            SvgImage(
                asset = "eye",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(10.sdp)
            )

            Spacer(Modifier.width(5.sdp))

            Text(
                text = viewers.toString(),
                fontSize = 9.ssp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(Modifier.weight(1f))

        StreamTimer(
            status = status,
            modifier = Modifier
                .background(cardColors, shape)
                .padding(horizontal = 5.sdp)
        )
    }
}

private fun getButtonInfoAccordingly(streamStatus: StreamStatus?, colorScheme: ColorScheme) =
    when (streamStatus) {
        StreamStatus.SETTING -> mapOf(
            "label" to R.string.setting_things_up,
            "color" to colorScheme.disabledContainer.copy(alpha = 0.5f),
            "labelColor" to Color.White
        )

        StreamStatus.PAUSED -> mapOf(
            "label" to R.string.paused,
            "color" to colorScheme.secondary,
            "labelColor" to colorScheme.onSecondary
        )

        else -> mapOf(
            "label" to R.string.live,
            "color" to Color.Red,
            "labelColor" to Color.White
        )
    }

@Preview
@Composable
private fun PreviewBroadcastTopOverlay() {
    MyApplicationTheme {
        Box {
            BroadcastTopOverlay(StreamStatus.LIVE, 100)
        }
    }
}