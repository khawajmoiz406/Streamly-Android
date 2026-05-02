package com.livestreaming.streamly.ui.watch.domain.usecase

import com.livestreaming.streamly.base.FlowUseCase
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.watch.domain.repository.WatchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveStreamUseCase @Inject constructor(private val repo: WatchRepository) :
    FlowUseCase<Stream?, String> {
    override fun invoke(params: String): Flow<Stream?> {
        return repo.observeStream(params)
    }
}