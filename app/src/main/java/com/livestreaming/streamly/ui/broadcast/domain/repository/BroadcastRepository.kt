package com.livestreaming.streamly.ui.broadcast.domain.repository

import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.broadcast.data.remote.dto.ChangeStreamStatusRequest
import com.livestreaming.streamly.ui.broadcast.data.remote.dto.StartStreamRequest
import kotlinx.coroutines.flow.Flow

interface BroadcastRepository {
    fun observeStream(streamId: String): Flow<Stream?>
    suspend fun startStream(request: StartStreamRequest): Result<Stream>
    suspend fun endStream(stream: Stream): Result<Unit>
    suspend fun toggleMic(stream: Stream): Result<Stream>
    suspend fun toggleCamera(stream: Stream): Result<Stream>
    suspend fun changeStreamStatus(request: ChangeStreamStatusRequest): Result<Unit>
}