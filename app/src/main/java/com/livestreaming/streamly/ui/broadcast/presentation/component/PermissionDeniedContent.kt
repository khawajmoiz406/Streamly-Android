package com.livestreaming.streamly.ui.broadcast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContainer
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.config.utils.PermissionUtils
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PermissionsDeniedContent() {
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 20.sdp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.sdp)
                    .background(
                        color = MaterialTheme.colorScheme.disabledContainer,
                        shape = RoundedCornerShape(10.sdp)
                    )
            ) {
                SvgImage(
                    asset = "camera",
                    color = MaterialTheme.colorScheme.disabledContent,
                    modifier = Modifier.size(25.sdp, 25.sdp)
                )
            }

            Spacer(Modifier.height(15.sdp))

            Text(
                text = stringResource(R.string.camera_access_required),
                fontSize = 14.ssp,
                lineHeight = 14.ssp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
            )

            Spacer(Modifier.height(5.sdp))

            Text(
                text = stringResource(R.string.camera_access_required_msg),
                fontSize = 10.ssp,
                lineHeight = 10.ssp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.disabledContent,
            )

            Spacer(Modifier.height(15.sdp))

            Button(
                onClick = { PermissionUtils.openAppSettings(context) },
                shape = RoundedCornerShape(8.sdp),
                modifier = Modifier.height(35.sdp),
            ) {
                Text(
                    stringResource(R.string.goto_settings),
                    fontSize = 12.ssp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewPermissionsDeniedContent() {
    MyApplicationTheme {
        PermissionsDeniedContent()
    }
}