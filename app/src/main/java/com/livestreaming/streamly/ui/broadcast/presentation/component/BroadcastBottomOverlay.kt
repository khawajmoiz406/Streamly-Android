package com.livestreaming.streamly.ui.broadcast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.button.AppLoadingButton
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.components.layout.StreamComments
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContainer
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.core.model.Comment
import com.livestreaming.streamly.core.model.StreamStatus
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun BoxScope.BroadcastBottomOverlay(
    title: String,
    isMuted: Boolean,
    isInPipMode: Boolean,
    isEndingStream: Boolean,
    isCameraDisabled: Boolean,
    status: StreamStatus?,
    comments: List<Comment>?,
    onMicrophoneClicked: () -> Unit,
    onCameraClicked: () -> Unit,
    onChangeCameraClicked: () -> Unit,
    onEndClicked: () -> Unit,
) {
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color.Transparent,
                        0.7f to Color.Black.copy(alpha = 0.4f),
                        1.0f to Color.Black.copy(alpha = 0.8f)
                    )
                )
            )
            .padding(10.sdp)
            .align(Alignment.BottomCenter)
    ) {

        if (isInPipMode) return@Column

        comments?.let {
            key(it) { StreamComments(it) }

            Spacer(Modifier.height(10.sdp))
        }

        Text(
            text = title,
            fontSize = 13.ssp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )

        Spacer(Modifier.height(10.sdp))

        Row(horizontalArrangement = Arrangement.spacedBy(15.sdp)) {
            IconButton(
                icon = if (isMuted) "micro_phone_disable" else "micro_phone",
                bg = if (isMuted) MaterialTheme.colorScheme.errorContainer else null,
                onClick = { onMicrophoneClicked.invoke() }
            )

            IconButton(
                icon = if (isCameraDisabled) "camera_disable" else "camera",
                bg = if (isCameraDisabled) MaterialTheme.colorScheme.errorContainer else null,
                onClick = { onCameraClicked.invoke() }
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
                buttonColor = MaterialTheme.colorScheme.errorContainer,
                labelColor = MaterialTheme.colorScheme.onErrorContainer,
                enabled = status != StreamStatus.Setting,
                shape = RoundedCornerShape(20.sdp),
                label = stringResource(R.string.end_stream),
                onClick = { onEndClicked.invoke() },
                modifier = Modifier
                    .padding(start = 45.sdp)
                    .height(35.sdp)
            )
        }

        Spacer(Modifier.height(navigationBarHeight))
    }
}

@Composable
private fun IconButton(
    icon: String,
    bg: Color?,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(
                if (!enabled) MaterialTheme.colorScheme.disabledContainer else (bg ?: Color.Transparent),
                CircleShape
            )
            .size(35.sdp)
            .clip(CircleShape)
            .clickable { if (enabled) onClick.invoke() }
    ) {
        SvgImage(
            asset = icon,
            color = if (!enabled) MaterialTheme.colorScheme.disabledContent else Color.White,
            modifier = Modifier.size(22.sdp)
        )
    }
}

@Preview
@Composable
private fun PreviewBroadcastBottomOverlay() {
    MyApplicationTheme {
        Box {
            BroadcastBottomOverlay(
                title = "Building a SaaS in 24 hours",
                isMuted = false,
                isInPipMode = false,
                isCameraDisabled = false,
                isEndingStream = false,
                status = StreamStatus.Live,
                onMicrophoneClicked = { },
                onCameraClicked = { },
                onChangeCameraClicked = { },
                onEndClicked = { },
                comments = listOf(
                    Comment(
                        id = "1",
                        userId = "1",
                        userName = "Test User 1",
                        message = "Hello how are you?"
                    ),
                    Comment(
                        id = "2",
                        userId = "2",
                        userName = "Test User 2",
                        message = "This is a very nice stream"
                    ),
                    Comment(
                        id = "3",
                        userId = "1",
                        userName = "Test User 1",
                        message = "Can you please explain this in details? I am having difficulty understanding the process"
                    ),
                )
            )
        }
    }
}