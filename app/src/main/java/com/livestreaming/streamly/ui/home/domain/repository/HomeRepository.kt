package com.livestreaming.streamly.ui.home.domain.repository

import com.livestreaming.streamly.core.model.Stream
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun observeLiveStreams(): Flow<List<Stream>?>
    suspend fun getLiveStreams(request: Unit): Result<List<Stream>?>
}