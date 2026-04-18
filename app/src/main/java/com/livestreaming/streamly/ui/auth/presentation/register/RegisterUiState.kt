package com.livestreaming.streamly.ui.auth.presentation.register

import android.net.Uri
import androidx.compose.runtime.Stable
import com.livestreaming.streamly.config.components.state.FieldState

@Stable
data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val profilePicture: Uri? = null,
    val name: FieldState = FieldState(),
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val phoneNumber: FieldState = FieldState(),
    val confirmPassword: FieldState = FieldState(),
)