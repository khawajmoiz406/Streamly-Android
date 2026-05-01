package com.livestreaming.streamly.ui.broadcast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.button.AppLoadingButton
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContainer
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.config.utils.extension.getInitials
import com.livestreaming.streamly.core.model.Comment
import com.livestreaming.streamly.core.model.StreamStatus
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun BoxScope.BroadcastBottomOverlay(
    title: String,
    isMuted: Boolean,
    isEndingStream: Boolean,
    isCameraDisabled: Boolean,
    status: StreamStatus?,
    comments: List<Comment>?,
    onMicrophoneClicked: () -> Unit,
    onCameraClicked: () -> Unit,
    onChangeCameraClicked: () -> Unit,
    onEndClicked: () -> Unit,
) {
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

        comments?.let {
            key(it) { CommentsLazyColumn(it) }

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
    }
}

@Composable
fun CommentsLazyColumn(comments: List<Comment>) {
    var itemHeightPx by remember { mutableIntStateOf(0) }
    val itemHeightDp = with(LocalDensity.current) { itemHeightPx.toDp() }
    val listState = rememberLazyListState()

    LaunchedEffect(comments.size) {
        if (comments.isNotEmpty()) {
            listState.animateScrollToItem(comments.lastIndex)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = if (itemHeightDp > 0.dp) itemHeightDp * 3 else Dp.Unspecified)
    ) {
        itemsIndexed(comments) { index, comment ->
            ItemComment(
                comment = comment,
                modifier = Modifier.onSizeChanged { size ->
                    if (index == 0) itemHeightPx = size.height
                }
            )
        }
    }
}

@Composable
fun ItemComment(comment: Comment, modifier: Modifier) {
    Column(modifier) {
        Row {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(26.sdp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                if (comment.userAvatar == null) {
                    Text(
                        text = comment.userName.getInitials(),
                        fontSize = 11.ssp,
                        lineHeight = 11.ssp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    AsyncImage(
                        model = comment.userAvatar,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(Modifier.width(10.sdp))

            Text(
                text = comment.message,
                color = Color.White,
                fontSize = 12.ssp,
                lineHeight = 12.ssp,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(20.sdp))
                    .padding(horizontal = 12.sdp, vertical = 5.sdp)
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(Modifier.height(10.sdp))
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
            modifier = Modifier.size(25.sdp)
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
                isCameraDisabled = false,
                isEndingStream = false,
                status = StreamStatus.Paused,
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