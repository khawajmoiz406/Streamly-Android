package com.livestreaming.streamly.ui.home.data.repository

import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.home.data.local.HomeLocalDataSource
import com.livestreaming.streamly.ui.home.data.remote.HomeRemoteDataSource
import com.livestreaming.streamly.ui.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteRepo: HomeRemoteDataSource,
    private val localRepo: HomeLocalDataSource
) : HomeRepository {
    override fun observeLiveStreams(): Flow<List<Stream>?> {
        return remoteRepo.observeLiveStreams()
    }

    override suspend fun getLiveStreams(request: Unit): Result<List<Stream>?> = try {
        Result.success(remoteRepo.getLiveStreams())
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}