package com.livestreaming.streamly.ui.home.data.repository

import com.livestreaming.streamly.ui.home.data.local.HomeLocalDataSource
import com.livestreaming.streamly.ui.home.data.remote.HomeRemoteDataSource
import com.livestreaming.streamly.ui.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteRepo: HomeRemoteDataSource,
    private val localRepo: HomeLocalDataSource
) : HomeRepository {
    override suspend fun getHome(request: Unit): Result<Unit?> = try {
        Result.success(Unit)
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}