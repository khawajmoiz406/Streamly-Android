package com.livestreaming.streamly.ui.watch.data.remote.dto

import com.livestreaming.streamly.base.BaseRequest
import com.livestreaming.streamly.core.model.User

data class AddCommentRequest(
    val streamId: String,
    val comment: String,
    val user: User,
) : BaseRequest() {
    override fun toMap(): HashMap<String, Any> = hashMapOf()
}
