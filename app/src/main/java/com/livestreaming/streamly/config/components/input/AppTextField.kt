package com.livestreaming.streamly.config.components.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.livestreaming.streamly.config.components.image.SvgImage
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: String? = null,
    trailingIcon: String? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    fontSize: TextUnit = 13.ssp,
    height: Dp = 35.sdp,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 1.sdp,
    shape: RoundedCornerShape = RoundedCornerShape(10.sdp),
    imeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onImeActionPerformed: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.sdp, vertical = 0.sdp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    containerColor: Color = MaterialTheme.colorScheme.surface,
    error: String? = null,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        disabledContainerColor = containerColor,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    ),
) {
    val errorBorderColor = if (error != null) MaterialTheme.colorScheme.error else borderColor

    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            enabled = enabled,
            readOnly = readOnly,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .border(borderWidth, errorBorderColor, shape),
            textStyle = LocalTextStyle.current.copy(
                fontSize = fontSize,
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
                keyboardType = keyboardType
            ),
            keyboardActions = KeyboardActions(
                onAny = { onImeActionPerformed?.invoke() }
            ),
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    shape = shape,
                    contentPadding = contentPadding,
                    colors = colors,
                    placeholder = if (placeholder.isNotEmpty()) {
                        {
                            Text(
                                text = placeholder,
                                fontSize = fontSize,
                                color = MaterialTheme.colorScheme.outline.copy(0.5f)
                            )
                        }
                    } else null,
                    leadingIcon = leadingIcon?.let {
                        {
                            SvgImage(
                                asset = it,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(15.sdp)
                            )
                        }
                    },
                    trailingIcon = trailingIcon?.let {
                        {
                            SvgImage(
                                asset = it,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(15.sdp)
                                    .then(
                                        if (onTrailingIconClick != null)
                                            Modifier.clickable { onTrailingIconClick() }
                                        else Modifier
                                    )
                            )
                        }
                    },
                )
            }
        )

        AnimatedVisibility(visible = error != null) {
            Text(
                text = error.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                fontSize = 11.ssp,
                modifier = Modifier.padding(start = 4.sdp, top = 2.sdp)
            )
        }
    }
}