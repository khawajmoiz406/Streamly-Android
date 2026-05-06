package com.livestreaming.streamly.ui.profile.domain.usecase

import com.livestreaming.streamly.base.SuspendUseCase
import com.livestreaming.streamly.config.theme.ThemeMode
import com.livestreaming.streamly.ui.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ChangeThemeModeUseCase @Inject constructor(private val repo: ProfileRepository) :
    SuspendUseCase<Unit?, ThemeMode> {
    override suspend fun invoke(params: ThemeMode): Result<Unit?> {
        return repo.changeThemeMode(params)
    }
}