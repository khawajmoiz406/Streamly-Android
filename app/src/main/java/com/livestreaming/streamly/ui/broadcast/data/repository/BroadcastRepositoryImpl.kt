package com.livestreaming.streamly.ui.broadcast.data.repository

import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.model.StreamStatus
import com.livestreaming.streamly.ui.broadcast.data.local.BroadcastLocalDataSource
import com.livestreaming.streamly.ui.broadcast.data.remote.BroadcastRemoteDataSource
import com.livestreaming.streamly.ui.broadcast.domain.repository.BroadcastRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class BroadcastRepositoryImpl @Inject constructor(
    private val remoteRepo: BroadcastRemoteDataSource,
    private val localRepo: BroadcastLocalDataSource
) : BroadcastRepository {
    override fun observeStream(streamId: String): Flow<Stream?> {
        return remoteRepo.observeStream(streamId)
    }

    override suspend fun goLive(stream: Stream): Result<Stream> = try {
        val time = Calendar.getInstance().time

        val updatedStream = stream.copy(
            isLive = true,
            startedAt = time.time,
            status = StreamStatus.LIVE
        )

        remoteRepo.goLive(updatedStream)
        Result.success(updatedStream)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun endStream(stream: Stream): Result<Unit> = try {
        val time = Calendar.getInstance().time
        val updatedStream = stream.copy(isLive = false, endedAt = time.time, status = StreamStatus.ENDED)
        remoteRepo.endStream(updatedStream)
        Result.success(Unit)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun toggleMic(stream: Stream): Result<Stream> = try {
        val updatedStream = stream.copy(isMuted = !stream.isMuted)
        remoteRepo.toggleMic(stream)
        Result.success(updatedStream)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun toggleCamera(stream: Stream): Result<Stream> = try {
        val updatedStream = stream.copy(isCameraOff = !stream.isCameraOff)
        remoteRepo.toggleCamera(stream)
        Result.success(updatedStream)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun togglePlayPause(stream: Stream): Result<Stream> = try {
        val updatedStream = stream.copy(
            status = if (stream.status == StreamStatus.PAUSED) StreamStatus.LIVE else StreamStatus.PAUSED
        )
        remoteRepo.toggleStatus(stream)
        Result.success(updatedStream)
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}