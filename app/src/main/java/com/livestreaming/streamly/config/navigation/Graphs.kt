package com.livestreaming.streamly.config.navigation

import SplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.livestreaming.streamly.config.utils.extension.toNavType
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.auth.presentation.login.LoginScreen
import com.livestreaming.streamly.ui.auth.presentation.register.RegisterScreen
import com.livestreaming.streamly.ui.broadcast.presentation.BroadcastScreen
import com.livestreaming.streamly.ui.dashboard.DashboardScreen
import com.livestreaming.streamly.ui.home.presentation.HomeScreen
import com.livestreaming.streamly.ui.setup.presentation.SetupScreen
import kotlin.reflect.typeOf

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

fun NavGraphBuilder.bottomNavGraph() = navigation<Destination.BottomNavGraph>(
    startDestination = Destination.Home
) {
    composable<Destination.Home> { HomeScreen() }
    composable<Destination.Profile> { }
}

fun NavGraphBuilder.mainGraph() = navigation<Destination.MainGraph>(
    startDestination = Destination.Dashboard
) {
    composable<Destination.Dashboard> { DashboardScreen() }
    composable<Destination.StreamSetup> { SetupScreen() }
    composable<Destination.StreamViewer> { }
    composable<Destination.StreamBroadcast>() {
        val route = it.toRoute<Destination.StreamBroadcast>()
        BroadcastScreen(route.streamId)
    }
}