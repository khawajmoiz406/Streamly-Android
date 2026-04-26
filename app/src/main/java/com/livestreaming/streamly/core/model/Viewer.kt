package com.livestreaming.streamly.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Viewer(
    val userName: String,
    val joinedAt: Long,
    val lastActiveAt: Long,
    val isActive: Boolean,
    val photoUrl: String? = null,
)