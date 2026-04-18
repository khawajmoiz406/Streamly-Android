package com.livestreaming.streamly.config.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.disabledContent
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun AppLoadingButton(
    modifier: Modifier,
    label: String,
    loading: Boolean,
    onClick: () -> Unit,
    leadingIcon: String? = null,
    trailingIcon: String? = null,
    leadingIconColor: Color? = null,
    trailingIconColor: Color? = null,
    shape: Shape = RoundedCornerShape(8.sdp),
    labelColor: Color = MaterialTheme.colorScheme.onPrimary,
    buttonColor: Color = MaterialTheme.colorScheme.primary
) {
    stringResource(R.string.login)
    Button(
        onClick = { if(!loading) onClick.invoke() },
        shape = shape,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues.Zero,
        colors = ButtonDefaults.buttonColors().copy(containerColor = buttonColor)
    ) {
        when (loading) {
            false -> {
                leadingIcon?.let {
                    SvgImage(
                        asset = it,
                        color = leadingIconColor,
                        modifier = Modifier.size(15.sdp)
                    )

                    Spacer(Modifier.width(10.sdp))
                }

                Text(
                    text = label,
                    fontSize = 13.ssp,
                    color = labelColor
                )

                trailingIcon?.let {
                    Spacer(Modifier.width(10.sdp))

                    SvgImage(
                        asset = it,
                        color = trailingIconColor,
                        modifier = Modifier.size(15.sdp)
                    )
                }
            }

            true -> CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = labelColor
            )
        }
    }
}