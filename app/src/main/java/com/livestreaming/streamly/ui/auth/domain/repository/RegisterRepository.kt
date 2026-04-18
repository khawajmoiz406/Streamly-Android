package com.livestreaming.streamly.ui.auth.domain.repository

import com.livestreaming.streamly.core.model.User
import com.livestreaming.streamly.ui.auth.data.remote.dto.RegisterRequest

interface RegisterRepository {
    suspend fun register(request: RegisterRequest): Result<User?>
}