package com.livestreaming.streamly.ui.broadcast.data.remote.dto

import com.livestreaming.streamly.base.BaseRequest
import com.livestreaming.streamly.core.model.User

data class StartStreamRequest(
    val title: String,
    val desc: String? = "",
    val user: User,
) : BaseRequest() {
    override fun toMap(): HashMap<String, Any> = hashMapOf()
}
