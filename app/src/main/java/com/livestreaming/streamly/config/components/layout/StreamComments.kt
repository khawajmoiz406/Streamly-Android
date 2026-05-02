package com.livestreaming.streamly.config.components.layout

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalWindowInfo
import com.livestreaming.streamly.core.model.Comment
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@SuppressLint("FrequentlyChangingValue")
@Composable
fun StreamComments(comments: List<Comment>) {
    val listState = rememberLazyListState()
    val halfScreenHeight = LocalWindowInfo.current.containerDpSize.height * 0.3f

    LaunchedEffect(comments.size) {
        if (comments.isNotEmpty()) listState.scrollToItem(0)
    }

    LazyColumn(
        state = listState,
        reverseLayout = true,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = halfScreenHeight)
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()
                drawRect(
                    blendMode = BlendMode.DstIn,
                    brush = Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.3f to Color.Black.copy(alpha = 0.5f),
                        0.6f to Color.Black
                    ),
                )
            }
    ) {
        itemsIndexed(comments.reversed()) { _, comment ->
            ItemComment(comment = comment)
        }
    }
}

@Composable
fun ItemComment(comment: Comment, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row {
            UserImage(comment.userName, profilePicture = comment.userAvatar)

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
