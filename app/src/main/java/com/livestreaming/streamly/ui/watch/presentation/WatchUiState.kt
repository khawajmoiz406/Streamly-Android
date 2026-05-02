package com.livestreaming.streamly.ui.watch.presentation

import androidx.compose.runtime.Stable
import com.livestreaming.streamly.config.components.state.FieldState

@Stable
data class WatchUiState(
    val error: String = "",
    val isLoading: Boolean = false,
    val isAddingComment: Boolean = false,
    val comment: FieldState = FieldState(),
)