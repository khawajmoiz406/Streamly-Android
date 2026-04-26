package com.livestreaming.streamly.ui.broadcast.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.broadcast.domain.repository.BroadcastRepository
import javax.inject.Inject

class EndStreamUseCase @Inject constructor(private val repo: BroadcastRepository) :
    SuspendUseCase<Unit, Stream> {
    override suspend fun invoke(params: Stream): Result<Unit> {
        return repo.endStream(params)
    }
}