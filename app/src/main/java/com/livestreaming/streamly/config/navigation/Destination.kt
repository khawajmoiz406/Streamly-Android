package com.livestreaming.streamly.config.navigation

import kotlinx.serialization.Serializable

object Destination {
    @Serializable
    data object MainGraph

    @Serializable
    data object Splash

    @Serializable
    data object Home
}