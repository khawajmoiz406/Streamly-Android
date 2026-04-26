package com.livestreaming.streamly.ui.setup.data.remote.dto

import com.livestreaming.streamly.base.BaseRequest

data class StartStreamRequest(
    val title: String,
    val desc: String? = ""
) : BaseRequest() {
    override fun toMap(): HashMap<String, Any> = hashMapOf()
}
