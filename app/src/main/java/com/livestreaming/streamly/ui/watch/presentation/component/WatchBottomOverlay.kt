package com.livestreaming.streamly.ui.watch.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.button.AppLoadingButton
import com.livestreaming.streamly.config.components.input.AppTextField
import com.livestreaming.streamly.config.components.layout.StreamComments
import com.livestreaming.streamly.config.components.state.FieldState
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.core.model.Comment
import com.livestreaming.streamly.ui.watch.presentation.WatchUiState
import ir.kaaveh.sdpcompose.sdp

@Composable
fun BoxScope.WatchBottomOverlay(
    uiState: WatchUiState,
    comments: List<Comment>?,
    onSendClicked: () -> Unit,
    onFieldChange: (value: String, fieldUpdater: WatchUiState.(FieldState) -> WatchUiState) -> Unit
) {
    val focusManager = LocalFocusManager.current

    DisposableEffect(Unit) {
        onDispose { focusManager.clearFocus() }
    }

    Column(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color.Transparent,
                        0.7f to Color.Black.copy(alpha = 0.4f),
                        1.0f to Color.Black.copy(alpha = 0.8f)
                    )
                )
            )
            .padding(10.sdp)
            .align(Alignment.BottomCenter)
    ) {

        comments?.let {
            key(it) { StreamComments(it) }

            Spacer(Modifier.height(10.sdp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(5.sdp)) {
            AppTextField(
                value = uiState.comment.value,
                height = 30.sdp,
                singleLine = true,
                imeAction = ImeAction.Done,
                onValueChange = { onFieldChange.invoke(it) { state -> copy(comment = state) } },
                keyboardType = KeyboardType.Text,
                modifier = Modifier.weight(1f),
                placeholder = stringResource(R.string.say_something),
                containerColor = MaterialTheme.colorScheme.surface,
                borderColor = MaterialTheme.colorScheme.outline,
                onImeActionPerformed = { focusManager.clearFocus() },
                error = uiState.comment.error?.let { stringResource(it) },
            )

            AppLoadingButton(
                showTitle = false,
                iconSize = 18.sdp,
                trailingIcon = "send",
                onClick = onSendClicked,
                buttonColor = Color.Transparent,
                loading = uiState.isAddingComment,
                shape = RoundedCornerShape(20.sdp),
                modifier = Modifier.size(35.sdp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewWatchBottomOverlay() {
    MyApplicationTheme {
        Box {
            WatchBottomOverlay(
                uiState = WatchUiState(),
                comments = listOf(
                    Comment(
                        id = "1",
                        userId = "1",
                        userName = "Test User 1",
                        message = "Hello how are you?"
                    ),
                    Comment(
                        id = "2",
                        userId = "2",
                        userName = "Test User 2",
                        message = "This is a very nice stream"
                    ),
                    Comment(
                        id = "3",
                        userId = "1",
                        userName = "Test User 1",
                        message = "Can you please explain this in details? I am having difficulty understanding the process"
                    ),
                ),
                onSendClicked = { },
                onFieldChange = { _, _ -> }
            )
        }
    }
}