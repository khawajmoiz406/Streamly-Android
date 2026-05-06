package com.livestreaming.streamly.ui.profile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.disabledContainer
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.config.utils.DateTimeUtils
import com.livestreaming.streamly.core.model.Stream
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ItemRecentStream(stream: Stream) {
    val durationMs = (stream.endedAt - stream.startedAt).coerceAtLeast(0L)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.sdp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.sdp))
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.disabledContainer,
                shape = RoundedCornerShape(10.sdp),
            )
            .padding(10.sdp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.sdp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    RoundedCornerShape(8.sdp),
                ),
        ) {
            SvgImage(
                asset = "live",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.sdp),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stream.title,
                fontSize = 12.ssp,
                lineHeight = 12.ssp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(Modifier.height(2.sdp))

            Text(
                text = stringResource(
                    R.string.viewers_count_format,
                    stream.viewerCount,
                    DateTimeUtils.formatDurationShort(durationMs),
                    DateTimeUtils.formatStreamTimeAgo(stream.startedAt),
                ),
                fontSize = 9.ssp,
                lineHeight = 9.ssp,
                color = MaterialTheme.colorScheme.disabledContent,
            )
        }
    }
}
