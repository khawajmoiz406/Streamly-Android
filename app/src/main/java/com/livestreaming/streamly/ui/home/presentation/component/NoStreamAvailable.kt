package com.livestreaming.streamly.ui.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.button.AppLoadingButton
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContainer
import com.livestreaming.streamly.config.theme.disabledContent
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun NoStreamAvailable(onGoLiveClicked: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 10.sdp, vertical = 15.sdp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.disabledContainer, CircleShape)
                    .size(45.sdp)
                    .clip(CircleShape)
            ) {
                SvgImage(
                    asset = "live",
                    color = MaterialTheme.colorScheme.disabledContent,
                    modifier = Modifier.size(22.sdp)
                )
            }

            Spacer(Modifier.height(10.sdp))

            Text(
                text = stringResource(R.string.no_live_streams),
                fontSize = 14.ssp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(Modifier.height(5.sdp))

            Text(
                text = stringResource(R.string.no_live_streams_msg),
                fontSize = 11.ssp,
                lineHeight = 11.ssp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.disabledContent,
            )

            Spacer(Modifier.height(15.sdp))

            AppLoadingButton(
                loading = false,
                fontSize = 12.ssp,
                leadingIcon = "live",
                onClick = onGoLiveClicked,
                shape = RoundedCornerShape(5.sdp),
                label = stringResource(R.string.go_live),
                buttonColor = MaterialTheme.colorScheme.primary,
                labelColor = MaterialTheme.colorScheme.onPrimary,
                leadingIconColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .height(32.sdp)
                    .width(120.sdp)
            )
        }
    }
}

@Preview
@Composable
private fun NoStreamAvailablePreview() {
    MyApplicationTheme {
        NoStreamAvailable {}
    }
}