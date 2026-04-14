package com.livestreaming.streamly

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalView
import androidx.navigation.compose.rememberNavController
import com.livestreaming.streamly.config.navigation.NavGraph
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.utils.AppCompositionLocals.LocalParentNavController
import com.livestreaming.streamly.config.utils.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MainScreen() }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarUtils.init(snackbarHostState, scope)

    (LocalView.current.context as Activity).window.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isNavigationBarContrastEnforced = false
        }
    }

    MyApplicationTheme {
        CompositionLocalProvider(value = LocalParentNavController provides navController) {
            Scaffold(
                snackbarHost = { SnackbarUtils.CustomSnackbarHost(snackbarHostState) },
                content = { innerPadding -> NavGraph(navController, innerPadding) },
            )
        }
    }
}