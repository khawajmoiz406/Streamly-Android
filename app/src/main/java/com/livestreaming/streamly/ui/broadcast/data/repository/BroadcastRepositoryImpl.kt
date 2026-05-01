package com.livestreaming.streamly.ui.broadcast.data.repository

import com.livestreaming.streamly.core.model.Comment
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.model.StreamStatus
import com.livestreaming.streamly.ui.broadcast.data.local.BroadcastLocalDataSource
import com.livestreaming.streamly.ui.broadcast.data.remote.BroadcastRemoteDataSource
import com.livestreaming.streamly.ui.broadcast.data.remote.dto.ChangeStreamStatusRequest
import com.livestreaming.streamly.ui.broadcast.data.remote.dto.StartStreamRequest
import com.livestreaming.streamly.ui.broadcast.domain.repository.BroadcastRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

class BroadcastRepositoryImpl @Inject constructor(
    private val remoteRepo: BroadcastRemoteDataSource,
    private val localRepo: BroadcastLocalDataSource
) : BroadcastRepository {
    override fun observeStream(streamId: String): Flow<Stream?> {
        return remoteRepo.observeStream(streamId)
    }

    override fun observeStreamComments(streamId: String): Flow<List<Comment>?> {
        return remoteRepo.observeStreamComments(streamId)
    }

    override suspend fun startStream(request: StartStreamRequest): Result<Stream> = try {
        val date = Calendar.getInstance().time
        val agoraChannelId: String = UUID.randomUUID().toString().take(12)

        val stream = Stream(
            hostId = request.user.id.toString(),
            hostName = request.user.name ?: "",
            title = request.title,
            viewerCount = 0,
            createdAt = date.time,
            startedAt = date.time,
            agoraChannelId = agoraChannelId,
            hostPhotoUrl = request.user.avatar ?: "",
            description = request.desc ?: "",
            status = StreamStatus.Setting.value
        )

        val updatedStream = remoteRepo.startStream(stream)
        Result.success(updatedStream)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun endStream(stream: Stream): Result<Unit> = try {
        val time = Calendar.getInstance().time
        val updatedStream = stream.copy(
            endedAt = time.time,
            status = StreamStatus.Ended.value
        )
        remoteRepo.endStream(updatedStream)
        Result.success(Unit)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun toggleMic(stream: Stream): Result<Stream> = try {
        val updatedStream = stream.copy(muted = !stream.muted)
        remoteRepo.toggleMic(updatedStream)
        Result.success(updatedStream)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun toggleCamera(stream: Stream): Result<Stream> = try {
        val updatedStream = stream.copy(cameraOff = !stream.cameraOff)
        remoteRepo.toggleCamera(updatedStream)
        Result.success(updatedStream)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun changeStreamStatus(request: ChangeStreamStatusRequest): Result<Unit> = try {
        remoteRepo.toggleStatus(request.streamId, request.status)
        Result.success(Unit)
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}