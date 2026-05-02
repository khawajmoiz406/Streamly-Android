package com.livestreaming.streamly.config.navigation

import kotlinx.serialization.Serializable

object Destination {

    //Landing routes
    @Serializable
    data object LandingGraph

    @Serializable
    data object Splash

    //Authentication routes
    @Serializable
    data object AuthGraph

    @Serializable
    data object Login

    @Serializable
    data object Register

    //Bottom nav routes
    @Serializable
    data object BottomNavGraph

    @Serializable
    data object Home

    @Serializable
    data object Profile

    //Main routes
    @Serializable
    data object MainGraph

    @Serializable
    data object Dashboard

    @Serializable
    data class StreamViewer(val streamId: String)

    @Serializable
    data object StreamBroadcast
}