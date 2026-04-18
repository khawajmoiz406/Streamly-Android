package com.livestreaming.streamly.ui.auth.data.repository

import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.ui.auth.data.local.AuthLocalDataSource
import com.livestreaming.streamly.ui.auth.data.remote.AuthRemoteDataSource
import com.livestreaming.streamly.ui.auth.data.remote.dto.LoginRequest
import com.livestreaming.streamly.ui.auth.data.remote.dto.RegisterRequest
import com.livestreaming.streamly.ui.auth.domain.repository.LoginRepository
import com.livestreaming.streamly.ui.auth.domain.repository.RegisterRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteRepo: AuthRemoteDataSource,
    private val localRepo: AuthLocalDataSource
) : LoginRepository, RegisterRepository {
    override suspend fun login(request: LoginRequest) = try {
        val user = remoteRepo.login(request)
        localRepo.saveUserModel(user)

        Result.success(user)
    } catch (ex: ApiException) {
        Result.failure(ex)
    }

    override suspend fun sendResetPasswordLink(email: String) = try {
        remoteRepo.sendResetPasswordLink(email)
        Result.success(Unit)
    } catch (ex: ApiException) {
        Result.failure(ex)
    }

    override suspend fun register(request: RegisterRequest) = try {
        val user = remoteRepo.register(request)
        localRepo.saveUserModel(user)

        Result.success(user)
    } catch (ex: ApiException) {
        Result.failure(ex)
    }
}