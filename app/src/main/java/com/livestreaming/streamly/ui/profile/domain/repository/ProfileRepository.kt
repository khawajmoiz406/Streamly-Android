package com.livestreaming.streamly.ui.profile.domain.repository

import com.livestreaming.streamly.config.theme.ThemeMode
import com.livestreaming.streamly.core.model.ProfileData
import com.livestreaming.streamly.core.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfileData(userId: String): Flow<ProfileData>
    suspend fun signOut(): Result<Unit>
    suspend fun changeThemeMode(themeMode: ThemeMode): Result<Unit?>
}