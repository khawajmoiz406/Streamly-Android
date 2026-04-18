package com.livestreaming.streamly.config.navigation

import SplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.livestreaming.streamly.ui.auth.presentation.login.LoginScreen
import com.livestreaming.streamly.ui.auth.presentation.register.RegisterScreen
import com.livestreaming.streamly.ui.home.presentation.HomeScreen

fun NavGraphBuilder.landingGraph() = navigation<Destination.LandingGraph>(
    startDestination = Destination.Splash
) {
    composable<Destination.Splash> { SplashScreen() }
}

fun NavGraphBuilder.authGraph() = navigation<Destination.AuthGraph>(
    startDestination = Destination.Login
) {
    composable<Destination.Login> { LoginScreen() }
    composable<Destination.Register> { RegisterScreen() }
}

fun NavGraphBuilder.mainGraph() = navigation<Destination.MainGraph>(
    startDestination = Destination.Home
) {
    composable<Destination.Home> { HomeScreen() }
}