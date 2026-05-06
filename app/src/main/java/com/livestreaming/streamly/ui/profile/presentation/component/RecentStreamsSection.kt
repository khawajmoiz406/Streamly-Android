package com.livestreaming.streamly.ui.profile.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.livestream.streamly.R
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.core.model.Stream
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun RecentStreamsSection(streams: List<Stream>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.recent_streams),
            fontSize = 13.ssp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(Modifier.height(8.sdp))

        if (streams.isEmpty()) {
            Text(
                text = stringResource(R.string.no_streams_yet),
                fontSize = 11.ssp,
                color = MaterialTheme.colorScheme.disabledContent,
                modifier = Modifier.padding(vertical = 12.sdp),
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.sdp)) {
                streams.forEach { ItemRecentStream(it) }
            }
        }
    }
}
