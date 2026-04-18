package com.livestreaming.streamly.ui.auth.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.core.model.User
import com.livestreaming.streamly.ui.auth.data.remote.dto.LoginRequest
import com.livestreaming.streamly.ui.auth.domain.repository.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repo: LoginRepository) :
    SuspendUseCase<User?, LoginRequest> {
    override suspend fun invoke(params: LoginRequest): Result<User?> {
        return repo.login(params)
    }
}