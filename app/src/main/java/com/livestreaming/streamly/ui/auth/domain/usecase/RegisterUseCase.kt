package com.livestreaming.streamly.ui.auth.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.core.model.User
import com.livestreaming.streamly.ui.auth.data.remote.dto.RegisterRequest
import com.livestreaming.streamly.ui.auth.domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repo: RegisterRepository) :
    SuspendUseCase<User?, RegisterRequest> {
    override suspend fun invoke(params: RegisterRequest): Result<User?> {
        return repo.register(params)
    }
}