package com.livestreaming.streamly.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Viewer(
    val userId: String = "",
    val userName: String = "",
    val joinedAt: Long = 0L,
    val lastActiveAt: Long = 0L,
    val active: Boolean = false,
    val photoUrl: String? = null,
)