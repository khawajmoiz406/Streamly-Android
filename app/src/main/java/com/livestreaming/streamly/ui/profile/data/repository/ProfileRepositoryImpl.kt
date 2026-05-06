package com.livestreaming.streamly.ui.profile.data.repository

import com.livestreaming.streamly.config.theme.ThemeMode
import com.livestreaming.streamly.core.model.ProfileData
import com.livestreaming.streamly.core.model.User
import com.livestreaming.streamly.ui.profile.data.local.ProfileLocalDataSource
import com.livestreaming.streamly.ui.profile.data.remote.ProfileRemoteDataSource
import com.livestreaming.streamly.ui.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val remoteRepo: ProfileRemoteDataSource,
    private val localRepo: ProfileLocalDataSource,
) : ProfileRepository {

    override fun observeProfileData(userId: String): Flow<ProfileData> {
        return remoteRepo.observeProfileData(userId)
    }

    override suspend fun changeThemeMode(themeMode: ThemeMode): Result<Unit?> = try {
        val user = localRepo.changeThemeMode(themeMode)
        Result.success(user)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun signOut(): Result<Unit> = try {
        remoteRepo.signOut()
        localRepo.clearUser()
        Result.success(Unit)
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}