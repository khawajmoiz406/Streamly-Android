package com.livestreaming.streamly.ui.watch.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.core.model.Comment
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.watch.data.remote.dto.AddCommentRequest
import com.livestreaming.streamly.ui.watch.domain.repository.WatchRepository
import javax.inject.Inject

class GetStreamUseCase @Inject constructor(private val repo: WatchRepository) :
    SuspendUseCase<Stream?, String> {
    override suspend fun invoke(params: String): Result<Stream?> {
        return repo.getStream(params)
    }
}