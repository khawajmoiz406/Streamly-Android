package com.livestreaming.streamly.ui.setup.domain.repository

import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.setup.data.remote.dto.StartStreamRequest

interface SetupRepository {
    suspend fun startStream(request: StartStreamRequest): Result<Stream?>
}