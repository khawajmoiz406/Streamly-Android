package com.livestreaming.streamly.ui.broadcast.domain.repository

import com.livestreaming.streamly.core.model.Stream
import kotlinx.coroutines.flow.Flow

interface BroadcastRepository {
    fun observeStream(streamId: String): Flow<Stream?>
    suspend fun goLive(stream: Stream): Result<Stream>
    suspend fun endStream(stream: Stream): Result<Unit>
    suspend fun toggleMic(stream: Stream): Result<Stream>
    suspend fun toggleCamera(stream: Stream): Result<Stream>
    suspend fun togglePlayPause(stream: Stream): Result<Stream>
}