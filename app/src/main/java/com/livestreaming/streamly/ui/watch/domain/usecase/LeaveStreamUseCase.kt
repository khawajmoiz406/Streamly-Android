package com.livestreaming.streamly.ui.watch.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.watch.data.remote.dto.StreamUserRequest
import com.livestreaming.streamly.ui.watch.domain.repository.WatchRepository
import javax.inject.Inject

class LeaveStreamUseCase @Inject constructor(private val repo: WatchRepository) :
    SuspendUseCase<Stream, StreamUserRequest> {
    override suspend fun invoke(params: StreamUserRequest): Result<Stream> {
        return repo.leaveStream(params)
    }
}