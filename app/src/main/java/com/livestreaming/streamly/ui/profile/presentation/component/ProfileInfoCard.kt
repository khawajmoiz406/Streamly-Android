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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.disabledContainer
import com.livestreaming.streamly.config.theme.disabledContent
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ProfileInfoCard(
    label: String,
    value: String,
    iconAsset: String,
    iconTint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.sdp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 55.sdp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.sdp))
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.disabledContainer,
                shape = RoundedCornerShape(10.sdp),
            )
            .padding(horizontal = 12.sdp, vertical = 10.sdp),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(20.sdp)) {
            SvgImage(
                asset = iconAsset,
                color = iconTint,
                modifier = Modifier.size(18.sdp),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 9.ssp,
                lineHeight = 9.ssp,
                color = MaterialTheme.colorScheme.disabledContent,
            )

            Spacer(Modifier.height(2.sdp))

            Text(
                text = value,
                fontSize = 12.ssp,
                lineHeight = 12.ssp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        trailing?.invoke()
    }
}
