package com.livestreaming.streamly.ui.home.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetLiveStreamsUseCase @Inject constructor(private val repo: HomeRepository) :
    SuspendUseCase<List<Stream>?, Unit> {
    override suspend fun invoke(params: Unit): Result<List<Stream>?> {
        return repo.getLiveStreams(params)
    }
}