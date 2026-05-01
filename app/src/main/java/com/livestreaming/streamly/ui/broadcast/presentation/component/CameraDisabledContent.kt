package com.livestreaming.streamly.ui.broadcast.presentation.component

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContainer
import com.livestreaming.streamly.config.theme.disabledContent
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun CameraDisabledContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 20.sdp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.disabledContainer, CircleShape)
                .size(50.sdp)
                .clip(CircleShape)
        ) {
            SvgImage(
                asset = "camera_disable",
                color = MaterialTheme.colorScheme.disabledContent,
                modifier = Modifier.size(25.sdp)
            )
        }

        Spacer(Modifier.height(10.sdp))

        Text(
            text = stringResource(R.string.camera_disabled),
            fontSize = 13.ssp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(Modifier.height(5.sdp))

        Text(
            text = stringResource(R.string.camera_disabled_msg),
            fontSize = 11.ssp,
            lineHeight = 11.ssp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.disabledContent,
        )
    }
}

@Preview
@Composable
private fun PreviewCameraDisabledContent() {
    MyApplicationTheme {
        CameraDisabledContent()
    }
}