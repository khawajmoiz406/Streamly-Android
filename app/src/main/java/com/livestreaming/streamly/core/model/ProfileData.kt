package com.livestreaming.streamly.core.model

import androidx.compose.runtime.Stable

@Stable
data class ProfileData(
    val streams: List<Stream> = emptyList(),
    val streamCount: Int = 0,
    val totalStreamTimeMs: Long = 0L,
    val avgViewers: Int = 0,
)
