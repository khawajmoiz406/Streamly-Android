package com.livestreaming.streamly.config.components.layout

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.livestreaming.streamly.core.model.Comment
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun StreamComments(comments: List<Comment>, maxComments: Int = 3) {
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
            .heightIn(max = if (itemHeightDp > 0.dp) itemHeightDp * maxComments else Dp.Unspecified)
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
