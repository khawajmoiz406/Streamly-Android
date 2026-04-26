package com.livestreaming.streamly.ui.setup.presentation

import androidx.compose.runtime.Stable
import com.livestreaming.streamly.config.components.state.FieldState

@Stable
data class SetupUiState(
    val error: String = "",
    val isLoading: Boolean = false,
    val title: FieldState = FieldState(),
    val description: FieldState = FieldState(),
)