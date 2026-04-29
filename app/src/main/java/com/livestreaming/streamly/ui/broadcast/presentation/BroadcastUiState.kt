package com.livestreaming.streamly.ui.broadcast.presentation

import androidx.compose.runtime.Stable
import com.livestreaming.streamly.config.components.state.FieldState

@Stable
data class BroadcastUiState(
    val error: String = "",
    val isCreatingStream: Boolean = false,
    val isEndingLiveStream: Boolean = false,
    val title: FieldState = FieldState(),
    val description: FieldState = FieldState(),
)