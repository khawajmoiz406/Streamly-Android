package com.livestreaming.streamly.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val message: String = "",
    val sentAt: Long = 0L,
    val userAvatar: String? = null,
)