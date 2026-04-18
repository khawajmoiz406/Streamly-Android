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

    //Main routes
    @Serializable
    data object MainGraph
    @Serializable
    data object Home
}