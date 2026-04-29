package com.livestreaming.streamly.core.model

import androidx.compose.runtime.Stable

@Stable
data class Stream(
    val id: String = "",
    val hostId: String = "",
    val hostName: String = "",
    val hostPhotoUrl: String = "",
    val agoraChannelId: String = "",
    val title: String = "",
    val description: String = "",
    val muted: Boolean = false,
    val cameraOff: Boolean = false,
    val viewerCount: Int = 0,
    val createdAt: Long = 0L,
    val startedAt: Long = 0L,
    val endedAt: Long = 0L,
    val status: String = StreamStatus.Setting.value
) {
    fun getStreamStatus() = when (status) {
        "setting" -> StreamStatus.Setting
        "paused" -> StreamStatus.Paused
        "live" -> StreamStatus.Live
        "ended" -> StreamStatus.Ended
        else -> StreamStatus.Setting
    }
}

sealed class StreamStatus(val value: String) {
    data object Setting : StreamStatus("setting")
    data object Paused : StreamStatus("paused")
    data object Live : StreamStatus("live")
    data object Ended : StreamStatus("ended")
}