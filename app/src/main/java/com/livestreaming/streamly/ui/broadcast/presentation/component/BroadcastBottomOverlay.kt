package com.livestreaming.streamly.ui.broadcast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.button.AppLoadingButton
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.core.model.StreamStatus
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun BoxScope.BroadcastBottomOverlay(
    isMuted: Boolean,
    isEndingStream: Boolean,
    isCameraDisabled: Boolean,
    status: StreamStatus?,
    onMicrophoneClicked: () -> Unit,
    onCameraClicked: () -> Unit,
    onPlayPauseClicked: () -> Unit,
    onChangeCameraClicked: () -> Unit,
    onEndClicked: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(15.sdp),
        modifier = Modifier
            .background(Color.Transparent)
            .padding(10.sdp)
            .align(Alignment.BottomCenter)
    ) {
        IconButton(
            icon = if (isMuted) "mirco_phone_disable" else "micro_phone",
            bg = if (isMuted) Color.Red else Color.Transparent,
            onClick = { onMicrophoneClicked.invoke() }
        )

        IconButton(
            icon = if (isCameraDisabled) "camera_disable" else "camera",
            bg = if (isCameraDisabled) Color.Red else Color.Transparent,
            onClick = { onCameraClicked.invoke() }
        )

        IconButton(
            icon = if (status == StreamStatus.PAUSED) "play" else "pause",
            bg = if (status == StreamStatus.PAUSED) MaterialTheme.colorScheme.secondary else Color.Transparent,
            onClick = { onPlayPauseClicked.invoke() }
        )

        IconButton(
            icon = "rotate",
            bg = Color.Transparent,
            onClick = { onChangeCameraClicked.invoke() }
        )

        Spacer(Modifier.weight(1f))

        AppLoadingButton(
            fontSize = 11.ssp,
            loading = isEndingStream,
            buttonColor = Color.Red,
            labelColor = Color.White,
            shape = RoundedCornerShape(15.sdp),
            label = stringResource(R.string.end_stream),
            onClick = { onEndClicked.invoke() },
            modifier = Modifier
                .padding(start = 20.sdp)
                .height(30.sdp)
        )
    }
}

@Composable
private fun IconButton(icon: String, bg: Color = Color.Transparent, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(bg, CircleShape)
            .size(30.sdp)
            .clip(CircleShape)
            .clickable { onClick.invoke() }
    ) {
        SvgImage(
            asset = icon,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.sdp)
        )
    }
}

@Preview
@Composable
private fun PreviewBroadcastBottomOverlay() {
    MyApplicationTheme {
        Box {
            BroadcastBottomOverlay(
                isMuted = false,
                isCameraDisabled = false,
                isEndingStream = false,
                status = StreamStatus.PAUSED,
                onMicrophoneClicked = { },
                onCameraClicked = { },
                onPlayPauseClicked = { },
                onChangeCameraClicked = { },
                onEndClicked = { }
            )
        }
    }
}