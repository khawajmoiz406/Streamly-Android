package com.livestreaming.streamly.ui.broadcast.domain.usecase

import com.livestreaming.streamly.base.FlowUseCase
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.broadcast.domain.repository.BroadcastRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveStreamUseCase @Inject constructor(private val repo: BroadcastRepository) :
    FlowUseCase<Stream?, String> {
    override fun invoke(params: String): Flow<Stream?> {
        return repo.observeStream(params)
    }
}