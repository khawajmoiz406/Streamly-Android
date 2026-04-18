package com.livestreaming.streamly.ui.auth.presentation.login.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.input.AppTextField
import com.livestreaming.streamly.config.components.state.FieldState
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.ui.auth.presentation.login.LoginUiState
import ir.kaaveh.sdpcompose.sdp

@Composable
fun LoginForm(
    uiState: LoginUiState,
    onFieldChange: (value: String, fieldUpdater: LoginUiState.(FieldState) -> LoginUiState) -> Unit
) {
    val passwordVisible = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    DisposableEffect(Unit) {
        onDispose { focusManager.clearFocus() }
    }

    Column {
        AppTextField(
            value = uiState.email.value,
            label = stringResource(R.string.email),
            height = 35.sdp,
            leadingIcon = "email",
            imeAction = ImeAction.Next,
            onValueChange = { onFieldChange.invoke(it) { state -> copy(email = state) } },
            keyboardType = KeyboardType.Email,
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(R.string.email_hint),
            containerColor = MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.outline,
            onImeActionPerformed = { focusManager.moveFocus(FocusDirection.Next) },
            error = uiState.email.error?.let { stringResource(it) },
        )

        Spacer(Modifier.height(10.sdp))

        AppTextField(
            value = uiState.password.value,
            label = stringResource(R.string.password),
            height = 35.sdp,
            leadingIcon = "lock",
            imeAction = ImeAction.Done,
            onValueChange = { onFieldChange.invoke(it) { state -> copy(password = state) } },
            keyboardType = KeyboardType.Password,
            borderColor = MaterialTheme.colorScheme.outline,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = if (passwordVisible.value) "eye_closed" else "eye_open",
            visualTransformation = if (!passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
            placeholder = stringResource(R.string.password_hint),
            containerColor = MaterialTheme.colorScheme.surface,
            onImeActionPerformed = { focusManager.clearFocus() },
            error = uiState.password.error?.let { stringResource(it) },
            onTrailingIconClick = { passwordVisible.value = !passwordVisible.value }
        )
    }
}


@Preview
@Composable
private fun PreviewLoginForm() {
    MyApplicationTheme {
        LoginForm(LoginUiState()) { _, _ -> }
    }
}