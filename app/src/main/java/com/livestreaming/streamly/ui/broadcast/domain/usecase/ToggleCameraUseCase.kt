package com.livestreaming.streamly.ui.broadcast.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.broadcast.domain.repository.BroadcastRepository
import javax.inject.Inject

class ToggleCameraUseCase @Inject constructor(private val repo: BroadcastRepository) :
    SuspendUseCase<Stream, Stream> {
    override suspend fun invoke(params: Stream): Result<Stream> {
        return repo.toggleCamera(params)
    }
}