package com.livestreaming.streamly.ui.watch.data.remote.dto

import com.livestreaming.streamly.base.BaseRequest
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.model.User

data class StreamUserRequest(
    val stream: Stream,
    val user: User,
) : BaseRequest() {
    override fun toMap(): HashMap<String, Any> = hashMapOf()
}
