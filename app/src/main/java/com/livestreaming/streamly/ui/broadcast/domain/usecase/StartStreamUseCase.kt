package com.livestreaming.streamly.ui.broadcast.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.broadcast.data.remote.dto.StartStreamRequest
import com.livestreaming.streamly.ui.broadcast.domain.repository.BroadcastRepository
import javax.inject.Inject

class StartStreamUseCase @Inject constructor(private val repo: BroadcastRepository) :
    SuspendUseCase<Stream, StartStreamRequest> {
    override suspend fun invoke(params: StartStreamRequest): Result<Stream> {
        return repo.startStream(params)
    }
}