package com.livestreaming.streamly.core.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Stable
data class Stream(
    val id: String,
    val hostId: String,
    val hostName: String,
    val title: String,
    val isLive: Boolean,
    val viewerCount: Int,
    val createdAt: Long,
    val endedAt: Long? = null,
    val startedAt: Long? = null,
    val isMuted: Boolean = false,
    val isCameraOff: Boolean = false,
    val hostPhotoUrl: String? = null,
    val description: String? = null,
    val comments: List<Comment>?,
    val viewers: List<Viewer>?,
    val status: StreamStatus = StreamStatus.SETTING,
    val agoraChannelId: String = UUID.randomUUID().toString().take(12),
)

enum class StreamStatus {
    SETTING,
    PAUSED,
    LIVE,
    ENDED,
}