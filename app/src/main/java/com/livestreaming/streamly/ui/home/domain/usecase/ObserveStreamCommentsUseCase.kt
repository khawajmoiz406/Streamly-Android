package com.livestreaming.streamly.ui.home.domain.usecase

import com.livestreaming.streamly.base.FlowUseCase
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLiveStreamsUseCase @Inject constructor(private val repo: HomeRepository) :
    FlowUseCase<List<Stream>?, Unit> {
    override fun invoke(params: Unit): Flow<List<Stream>?> {
        return repo.observeLiveStreams()
    }
}