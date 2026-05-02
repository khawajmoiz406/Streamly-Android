package com.livestreaming.streamly.ui.watch.domain.repository

import com.livestreaming.streamly.core.model.Comment
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.watch.data.remote.dto.AddCommentRequest
import com.livestreaming.streamly.ui.watch.data.remote.dto.StreamUserRequest
import kotlinx.coroutines.flow.Flow

interface WatchRepository {
    fun observeStream(streamId: String): Flow<Stream?>
    fun observeStreamComments(streamId: String): Flow<List<Comment>?>
    suspend fun getStream(streamId: String): Result<Stream?>
    suspend fun userJoinedStream(request: StreamUserRequest): Result<Stream>
    suspend fun leaveStream(request: StreamUserRequest): Result<Stream>
    suspend fun addCommentToStream(request: AddCommentRequest): Result<Comment>

}