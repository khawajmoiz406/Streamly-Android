package com.livestreaming.streamly.ui.setup.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.setup.data.remote.dto.StartStreamRequest
import com.livestreaming.streamly.ui.setup.domain.repository.SetupRepository
import javax.inject.Inject

class StartStreamUseCase @Inject constructor(private val repo: SetupRepository) :
    SuspendUseCase<Stream?, StartStreamRequest> {
    override suspend fun invoke(params: StartStreamRequest): Result<Stream?> {
        return repo.startStream(params)
    }
}