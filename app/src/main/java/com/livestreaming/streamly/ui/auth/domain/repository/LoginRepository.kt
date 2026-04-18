package com.livestreaming.streamly.ui.auth.domain.repository

import com.livestreaming.streamly.core.model.User
import com.livestreaming.streamly.ui.auth.data.remote.dto.LoginRequest

interface LoginRepository {
    suspend fun login(request: LoginRequest): Result<User?>
    suspend fun sendResetPasswordLink(email: String): Result<Unit?>
}