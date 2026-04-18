package com.livestreaming.streamly.ui.auth.presentation.register.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSelectionContent(onGalleryClicked: () -> Unit, onCameraClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.sdp)
    ) {
        Text(
            text = stringResource(R.string.select_a_photo),
            fontSize = 14.ssp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(10.sdp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.sdp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.sdp))
                .clickable { onGalleryClicked.invoke() }
                .padding(vertical = 12.sdp),
        ) {
            SvgImage(
                asset = "gallery",
                modifier = Modifier.size(22.sdp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.choose_from_gallery),
                fontSize = 13.ssp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(Modifier.height(5.sdp))

        HorizontalDivider()

        Spacer(Modifier.height(5.sdp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.sdp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.sdp))
                .clickable { onCameraClicked.invoke() }
                .padding(vertical = 12.sdp),
        ) {
            SvgImage(
                asset = "camera",
                modifier = Modifier.size(22.sdp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.take_a_photo),
                fontSize = 13.ssp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(Modifier.height(5.sdp))
    }
}

@Preview
@Composable
fun PreviewImageSelectionContent() {
    MyApplicationTheme {
        ImageSelectionContent({}, {})
    }
}