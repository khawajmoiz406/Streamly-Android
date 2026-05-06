package com.livestreaming.streamly.ui.profile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.disabledContainer
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.config.utils.DateTimeUtils
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun StatsRow(
    streamCount: Int,
    totalStreamTimeMs: Long,
    avgViewers: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.sdp),
    ) {
        StatCard(
            icon = "camera",
            value = "$streamCount",
            label = stringResource(R.string.stream_count_label),
            accent = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
        )

        StatCard(
            icon = "clock",
            value = DateTimeUtils.formatDurationShort(totalStreamTimeMs),
            label = stringResource(R.string.stream_time_label),
            accent = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.weight(1f),
        )

        StatCard(
            icon = "bar",
            value = "$avgViewers",
            label = stringResource(R.string.avg_viewers_label),
            accent = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatCard(
    icon: String,
    value: String,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.sdp))
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.disabledContainer,
                shape = RoundedCornerShape(10.sdp),
            )
            .padding(vertical = 12.sdp, horizontal = 8.sdp),
    ) {
        SvgImage(
            asset = icon,
            color = accent,
            modifier = Modifier.size(18.sdp),
        )

        Spacer(Modifier.height(6.sdp))

        Text(
            text = value,
            fontSize = 16.ssp,
            fontWeight = FontWeight.Bold,
            color = accent,
        )

        Spacer(Modifier.height(2.sdp))

        Text(
            text = label,
            fontSize = 9.ssp,
            color = MaterialTheme.colorScheme.disabledContent,
        )
    }
}