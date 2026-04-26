package com.livestreaming.streamly.ui.setup.data.repository

import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.setup.data.local.SetupLocalDataSource
import com.livestreaming.streamly.ui.setup.data.remote.SetupRemoteDataSource
import com.livestreaming.streamly.ui.setup.data.remote.dto.StartStreamRequest
import com.livestreaming.streamly.ui.setup.domain.repository.SetupRepository
import javax.inject.Inject

class SetupRepositoryImpl @Inject constructor(
    private val remoteRepo: SetupRemoteDataSource,
    private val localRepo: SetupLocalDataSource
) : SetupRepository {
    override suspend fun startStream(request: StartStreamRequest): Result<Stream?> = try {
        Result.success(remoteRepo.startStream(request))
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}