package com.livestreaming.streamly.config.components.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.disabledContent
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String? = null,
    onValueChange: (String) -> Unit,
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
    borderWidth: Dp = 0.5.dp,
    shape: RoundedCornerShape = RoundedCornerShape(10.sdp),
    imeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onImeActionPerformed: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.sdp, vertical = 0.sdp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    containerColor: Color = MaterialTheme.colorScheme.surface,
    error: String? = null,
    labelColor: Color? = null,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        disabledContainerColor = containerColor,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.primary
    ),
) {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val errorBorderColor = when {
        error != null -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.secondary
        else -> borderColor
    }

    Column(modifier = modifier) {
        label?.let {
            Text(
                text = it,
                color = labelColor ?: MaterialTheme.colorScheme.onSurface,
                fontSize = 11.ssp,
            )

            Spacer(Modifier.height(2.sdp))
        }


        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            enabled = enabled,
            readOnly = readOnly,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
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
                                color = MaterialTheme.colorScheme.disabledContent
                            )
                        }
                    } else null,
                    leadingIcon = leadingIcon?.let {
                        {
                            SvgImage(
                                asset = it,
                                color = MaterialTheme.colorScheme.disabledContent,
                                modifier = Modifier.size(15.sdp)
                            )
                        }
                    },
                    trailingIcon = trailingIcon?.let {
                        {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(30.sdp)
                                    .then(
                                        if (onTrailingIconClick != null)
                                            Modifier
                                                .clip(CircleShape)
                                                .clickable { onTrailingIconClick() }
                                        else Modifier
                                    )
                            ) {
                                SvgImage(
                                    asset = it,
                                    color = MaterialTheme.colorScheme.disabledContent,
                                    modifier = Modifier.size(15.sdp)
                                )
                            }
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