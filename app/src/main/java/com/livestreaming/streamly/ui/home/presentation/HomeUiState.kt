package com.livestreaming.streamly.ui.home.presentation

import androidx.compose.runtime.Stable

@Stable
data class HomeUiState(
    val error: String = "",
    val isLoading: Boolean = false,
    val showPermission: Boolean = false,
)