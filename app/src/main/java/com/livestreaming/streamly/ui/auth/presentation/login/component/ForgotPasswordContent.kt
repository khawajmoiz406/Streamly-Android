package com.livestreaming.streamly.ui.auth.presentation.login.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.button.AppLoadingButton
import com.livestreaming.streamly.config.components.input.AppTextField
import com.livestreaming.streamly.config.components.state.FieldState
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.ui.auth.presentation.login.LoginUiState
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ForgotPasswordContent(
    uiState: LoginUiState,
    onSendResetLinkClicked: () -> Unit,
    onFieldChange: (value: String, fieldUpdater: LoginUiState.(FieldState) -> LoginUiState) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 15.sdp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Text(
            text = stringResource(R.string.reset_password),
            fontSize = 18.ssp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            text = stringResource(R.string.reset_password_msg),
            color = MaterialTheme.colorScheme.disabledContent,
            fontSize = 11.ssp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(20.sdp))

        AppTextField(
            value = uiState.forgotEmail.value,
            height = 35.sdp,
            leadingIcon = "email",
            imeAction = ImeAction.Done,
            onValueChange = { onFieldChange.invoke(it) { state -> copy(forgotEmail = state) } },
            keyboardType = KeyboardType.Email,
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(R.string.email_hint),
            containerColor = MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.outline,
            onImeActionPerformed = { focusManager.clearFocus() },
            error = uiState.forgotEmail.error?.let { stringResource(it) },
        )

        Spacer(modifier = Modifier.height(15.sdp))

        AppLoadingButton(
            loading = uiState.isSendingResetLink,
            onClick = { onSendResetLinkClicked.invoke() },
            modifier = Modifier.height(35.sdp),
            label = stringResource(R.string.send_reset_link),
        )

        Spacer(modifier = Modifier.height(10.sdp))

    }
}

@Preview
@Composable
private fun PreviewForgotPasswordContent() {
    MyApplicationTheme {
        ForgotPasswordContent(LoginUiState(), {}, { _, _ -> })
    }
}