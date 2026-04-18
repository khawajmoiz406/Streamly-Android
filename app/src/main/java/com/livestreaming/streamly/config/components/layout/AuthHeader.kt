package com.livestreaming.streamly.config.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContent
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun AuthHeader(heading: String = stringResource(R.string.app_name), subHeading: String? = null) {
    val roundedCorners = RoundedCornerShape(10.sdp)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.sdp)
                    .background(MaterialTheme.colorScheme.primary, roundedCorners)
            ) {
                SvgImage(
                    asset = "live",
                    modifier = Modifier.size(25.sdp)
                )
            }

            Spacer(Modifier.height(10.sdp))

            Text(
                text = heading,
                fontSize = 18.ssp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )

            subHeading?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.disabledContent,
                    fontSize = 11.ssp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAuthHeader() {
    MyApplicationTheme {
        AuthHeader("Streamly", "Go live. Connect. Inspire.")
    }
}