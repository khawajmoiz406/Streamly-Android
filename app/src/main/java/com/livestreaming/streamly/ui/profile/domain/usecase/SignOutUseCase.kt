package com.livestreaming.streamly.ui.profile.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.ui.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val repo: ProfileRepository) :
    SuspendUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit): Result<Unit> {
        return repo.signOut()
    }
}
