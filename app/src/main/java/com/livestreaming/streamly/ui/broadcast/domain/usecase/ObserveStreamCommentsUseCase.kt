package com.livestreaming.streamly.ui.broadcast.domain.usecase

import com.livestreaming.streamly.base.FlowUseCase
import com.livestreaming.streamly.core.model.Comment
import com.livestreaming.streamly.ui.broadcast.domain.repository.BroadcastRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

    class ObserveStreamCommentsUseCase @Inject constructor(private val repo: BroadcastRepository) :
    FlowUseCase<List<Comment>?, String> {
    override fun invoke(params: String): Flow<List<Comment>?> {
        return repo.observeStreamComments(params)
    }
}