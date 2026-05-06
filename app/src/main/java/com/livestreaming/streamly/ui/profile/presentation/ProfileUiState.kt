package com.livestreaming.streamly.ui.profile.presentation

import androidx.compose.runtime.Stable

@Stable
data class ProfileUiState(
    val error: String = "",
    val isLoading: Boolean = false,
    val isSigningOut: Boolean = false,
    val loadingTheme: Boolean = false,
)