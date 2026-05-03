package com.livestreaming.streamly.ui.broadcast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.button.AppLoadingButton
import com.livestreaming.streamly.config.components.input.AppTextField
import com.livestreaming.streamly.config.components.state.FieldState
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.ui.broadcast.presentation.BroadcastUiState
import ir.kaaveh.sdpcompose.sdp

@Composable
fun BoxScope.StreamSetupOverlay(
    uiState: BroadcastUiState,
    onStartClicked: () -> Unit,
    onFieldChange: (value: String, fieldUpdater: BroadcastUiState.(FieldState) -> BroadcastUiState) -> Unit
) {
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val focusManager = LocalFocusManager.current

    DisposableEffect(Unit) {
        onDispose { focusManager.clearFocus() }
    }

    Column(
        Modifier
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(10.sdp)
            .align(Alignment.BottomCenter)
    ) {
        AppTextField(
            value = uiState.title.value,
            label = stringResource(R.string.stream_title),
            height = 35.sdp,
            imeAction = ImeAction.Next,
            onValueChange = { onFieldChange.invoke(it) { state -> copy(title = state) } },
            keyboardType = KeyboardType.Text,
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(R.string.stream_title_hint),
            containerColor = MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.outline,
            onImeActionPerformed = { focusManager.moveFocus(FocusDirection.Next) },
            error = uiState.title.error?.let { stringResource(it) },
        )

        Spacer(Modifier.height(10.sdp))

        AppTextField(
            value = uiState.description.value,
            label = stringResource(R.string.stream_description),
            height = 70.sdp,
            singleLine = false,
            contentPadding = PaddingValues(horizontal = 12.sdp, vertical = 8.sdp),
            imeAction = ImeAction.Done,
            onValueChange = { onFieldChange.invoke(it) { state -> copy(description = state) } },
            keyboardType = KeyboardType.Email,
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(R.string.stream_description_hint),
            containerColor = MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.outline,
            onImeActionPerformed = { focusManager.clearFocus() },
            error = uiState.description.error?.let { stringResource(it) },
        )

        Spacer(Modifier.height(15.sdp))

        AppLoadingButton(
            loading = uiState.isCreatingStream,
            onClick = { onStartClicked.invoke() },
            modifier = Modifier.height(35.sdp),
            label = stringResource(R.string.start_stream),
        )

        Spacer(Modifier.height(navigationBarHeight))
    }
}


@Preview
@Composable
private fun PreviewStreamSetupOverlay() {
    MyApplicationTheme {
        Box {
            StreamSetupOverlay(BroadcastUiState(), {}) { _, _ -> }
        }
    }
}