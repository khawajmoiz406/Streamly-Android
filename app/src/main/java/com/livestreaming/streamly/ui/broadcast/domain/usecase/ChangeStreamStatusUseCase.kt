package com.livestreaming.streamly.ui.broadcast.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.ui.broadcast.data.remote.dto.ChangeStreamStatusRequest
import com.livestreaming.streamly.ui.broadcast.domain.repository.BroadcastRepository
import javax.inject.Inject

class ChangeStreamStatusUseCase @Inject constructor(private val repo: BroadcastRepository) :
    SuspendUseCase<Unit, ChangeStreamStatusRequest> {
    override suspend fun invoke(params: ChangeStreamStatusRequest): Result<Unit> {
        return repo.changeStreamStatus(params)
    }
}