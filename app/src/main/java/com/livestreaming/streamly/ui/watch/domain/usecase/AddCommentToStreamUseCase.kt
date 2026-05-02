package com.livestreaming.streamly.ui.watch.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.core.model.Comment
import com.livestreaming.streamly.ui.watch.data.remote.dto.AddCommentRequest
import com.livestreaming.streamly.ui.watch.domain.repository.WatchRepository
import javax.inject.Inject

class AddCommentToStreamUseCase @Inject constructor(private val repo: WatchRepository) :
    SuspendUseCase<Comment, AddCommentRequest> {
    override suspend fun invoke(params: AddCommentRequest): Result<Comment> {
        return repo.addCommentToStream(params)
    }
}