package com.livestreaming.streamly.config.components.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.livestreaming.streamly.config.components.image.SvgImage
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun AppRadioButton(selected: Boolean, heading: String, onClick: () -> Unit, leadingIcon: String? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { onClick.invoke() }
        ),
    ) {
        if (leadingIcon != null) {
            SvgImage(
                asset = leadingIcon,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(18.sdp, 18.sdp)
            )

            Spacer(Modifier.width(8.sdp))
        }

        Text(
            text = heading,
            fontSize = 12.ssp,
            lineHeight = 12.ssp,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = selected,
            onClick = { onClick.invoke() },
        )
    }
}