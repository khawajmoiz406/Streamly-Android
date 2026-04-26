package com.livestreaming.streamly.ui.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.livestream.streamly.R
import com.livestreaming.streamly.config.navigation.Destination
import com.livestreaming.streamly.config.navigation.bottomNavGraph
import com.livestreaming.streamly.ui.dashboard.components.BottomNav
import ir.kaaveh.sdpcompose.sdp

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen() {
    val navController = rememberNavController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = { BottomNav(navController) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { _ ->
        Column {
            NavHost(
                navController = navController,
                startDestination = Destination.BottomNavGraph,
                modifier = Modifier.weight(1f),
                builder = { bottomNavGraph() }
            )

            Spacer(
                Modifier.height(
                    (integerResource(R.integer.bottom_nav_height).sdp) +
                            (WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding())
                )
            )
        }
    }
}