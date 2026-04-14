package com.livestreaming.streamly.config.navigation

import SplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.livestreaming.streamly.ui.home.presentation.HomeScreen

fun NavGraphBuilder.mainGraph() = navigation<Destination.MainGraph>(
    startDestination = Destination.Splash
) {
    composable<Destination.Splash> { SplashScreen() }
    composable<Destination.Home> { HomeScreen() }
}