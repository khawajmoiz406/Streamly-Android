package com.livestreaming.streamly

import android.app.Activity
import android.content.IntentFilter
import android.content.res.Configuration
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
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.livestreaming.streamly.config.navigation.NavGraph
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.ThemeState
import com.livestreaming.streamly.config.utils.AppCompositionLocals.LocalParentNavController
import com.livestreaming.streamly.config.utils.AppUtils
import com.livestreaming.streamly.config.utils.PictureInPictureUtils.ACTION_MUTE_CAMERA
import com.livestreaming.streamly.config.utils.PictureInPictureUtils.ACTION_TOGGLE_MIC
import com.livestreaming.streamly.config.utils.SnackbarUtils
import com.livestreaming.streamly.receiver.PictureInPictureReceiver
import com.livestreaming.streamly.receiver.PipEventBus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val pipReceiver = PictureInPictureReceiver()
    private val filter = IntentFilter().apply {
        addAction(ACTION_TOGGLE_MIC)
        addAction(ACTION_MUTE_CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ContextCompat.registerReceiver(
            this,
            pipReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        enableEdgeToEdge()
        setContent { MainScreen() }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        PipEventBus.emitPipState(isInPictureInPictureMode)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(pipReceiver)
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val isDarkTheme = AppUtils.isDarkTheme(navController.context)

    SnackbarUtils.init(snackbarHostState, scope)
    ThemeState.darkTheme.value = isDarkTheme

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