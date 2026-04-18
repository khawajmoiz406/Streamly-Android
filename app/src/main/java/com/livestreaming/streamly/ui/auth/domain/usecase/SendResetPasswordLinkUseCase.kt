package com.livestreaming.streamly.ui.auth.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.ui.auth.domain.repository.LoginRepository
import javax.inject.Inject

class SendResetPasswordLinkUseCase @Inject constructor(private val repo: LoginRepository) :
    SuspendUseCase<Unit?, String> {
    override suspend fun invoke(params: String): Result<Unit?> {
        return repo.sendResetPasswordLink(params)
    }
}