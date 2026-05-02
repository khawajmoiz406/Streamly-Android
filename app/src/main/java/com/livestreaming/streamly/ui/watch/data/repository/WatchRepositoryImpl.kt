package com.livestreaming.streamly.ui.watch.data.repository

import com.livestreaming.streamly.core.model.Comment
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.watch.data.local.WatchLocalDataSource
import com.livestreaming.streamly.ui.watch.data.remote.WatchRemoteDataSource
import com.livestreaming.streamly.ui.watch.data.remote.dto.AddCommentRequest
import com.livestreaming.streamly.ui.watch.data.remote.dto.StreamUserRequest
import com.livestreaming.streamly.ui.watch.domain.repository.WatchRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class WatchRepositoryImpl @Inject constructor(
    private val remoteRepo: WatchRemoteDataSource,
    private val localRepo: WatchLocalDataSource
) : WatchRepository {
    override fun observeStream(streamId: String): Flow<Stream?> {
        return remoteRepo.observeStream(streamId)
    }

    override fun observeStreamComments(streamId: String): Flow<List<Comment>?> {
        return remoteRepo.observeStreamComments(streamId)
    }

    override suspend fun getStream(streamId: String): Result<Stream?> = try {
        Result.success(remoteRepo.getStream(streamId))
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun userJoinedStream(request: StreamUserRequest): Result<Stream> = try {
        val updatedStream = request.stream.copy(viewerCount = (request.stream.viewerCount + 1))
        remoteRepo.userJoinedStream(updatedStream, request.user)
        Result.success(updatedStream)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun leaveStream(request: StreamUserRequest): Result<Stream> = try {
        val updatedStream = request.stream.copy(viewerCount = (request.stream.viewerCount - 1))
        remoteRepo.leaveStream(updatedStream, request.user)
        Result.success(updatedStream)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun addCommentToStream(request: AddCommentRequest): Result<Comment> = try {
        val date = Calendar.getInstance().time

        val comment = Comment(
            userId = request.user.id.toString(),
            userName = request.user.name ?: "",
            userAvatar = request.user.avatar,
            message = request.comment,
            sentAt = date.time,
        )

        val updatedStream = remoteRepo.addCommentToStream(request.streamId, comment)
        Result.success(updatedStream)
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}