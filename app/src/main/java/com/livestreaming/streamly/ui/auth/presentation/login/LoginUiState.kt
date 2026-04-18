package com.livestreaming.streamly.ui.auth.presentation.login

import androidx.compose.runtime.Stable
import com.livestreaming.streamly.config.components.state.FieldState

@Stable
data class LoginUiState(
    val isSigningWithEmail: Boolean = false,
    val isSigningWithGoogle: Boolean = false,
    val isSendingResetLink: Boolean = false,
    val error: String = "",
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val forgotEmail: FieldState = FieldState(),
)