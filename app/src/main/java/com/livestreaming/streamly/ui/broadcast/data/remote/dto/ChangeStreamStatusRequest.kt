package com.livestreaming.streamly.ui.broadcast.data.remote.dto

import com.livestreaming.streamly.base.BaseRequest
import com.livestreaming.streamly.core.model.StreamStatus

data class ChangeStreamStatusRequest(
    val streamId: String,
    val status: StreamStatus,
) : BaseRequest() {
    override fun toMap(): HashMap<String, Any> = hashMapOf()
}
