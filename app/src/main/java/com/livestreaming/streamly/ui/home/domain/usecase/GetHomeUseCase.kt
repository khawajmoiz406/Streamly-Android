package com.livestreaming.streamly.ui.home.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.ui.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetHomeUseCase @Inject constructor(private val repo: HomeRepository) :
    SuspendUseCase<Unit?, Unit> {
    override suspend fun invoke(params: Unit): Result<Unit?> {
        return repo.getHome(params)
    }
}