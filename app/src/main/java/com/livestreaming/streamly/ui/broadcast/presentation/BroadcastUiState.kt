package com.livestreaming.streamly.ui.broadcast.presentation

import androidx.compose.runtime.Stable

@Stable
data class BroadcastUiState(
    val error: String = "",
    val isEndingLiveStream: Boolean = false,
)