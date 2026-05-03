package com.livestreaming.streamly.config.components.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.livestreaming.streamly.config.components.button.AppLoadingButton
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContainer
import com.livestreaming.streamly.config.theme.disabledContent
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ConfirmationDialog(
    title: String,
    description: String,
    positiveButtonLabel: String,
    positionClick: () -> Unit,
    negativeButtonLabel: String? = null,
    negativeClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.sdp))
            .border(1.sdp, MaterialTheme.colorScheme.disabledContainer, RoundedCornerShape(8.sdp))
            .fillMaxWidth()
            .padding(horizontal = 10.sdp, vertical = 15.sdp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.disabledContainer, CircleShape)
                .size(45.sdp)
                .clip(CircleShape)
        ) {
            SvgImage(
                asset = "shield",
                color = MaterialTheme.colorScheme.disabledContent,
                modifier = Modifier.size(22.sdp)
            )
        }

        Spacer(Modifier.height(10.sdp))

        Text(
            text = title,
            fontSize = 14.ssp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(5.sdp))

        Text(
            text = description,
            fontSize = 11.ssp,
            lineHeight = 11.ssp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.disabledContent,
        )

        Spacer(Modifier.height(15.sdp))

        Row {
            if (negativeButtonLabel != null && negativeClick != null) {
                AppLoadingButton(
                    loading = false,
                    fontSize = 12.ssp,
                    onClick = negativeClick,
                    label = negativeButtonLabel,
                    buttonColor = Color.Transparent,
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(5.sdp),
                    border = BorderStroke(1.sdp, MaterialTheme.colorScheme.disabledContainer),
                    modifier = Modifier
                        .height(32.sdp)
                        .weight(1f),
                )

                Spacer(Modifier.width(10.sdp))
            }

            AppLoadingButton(
                loading = false,
                fontSize = 12.ssp,
                onClick = positionClick,
                label = positiveButtonLabel,
                shape = RoundedCornerShape(5.sdp),
                buttonColor = MaterialTheme.colorScheme.errorContainer,
                labelColor = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier
                    .height(32.sdp)
                    .weight(1f),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewConfirmationDialog() {
    MyApplicationTheme {
        ConfirmationDialog(
            "Alert",
            "Are you sure you want to perform this action?",
            "OK",
            {},
            "Cancel",
            {}
        )
    }
}