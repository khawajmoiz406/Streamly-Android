package com.livestreaming.streamly.ui.setup.presentation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.livestreaming.streamly.config.components.layout.AgoraCameraView
import com.livestreaming.streamly.config.navigation.Destination
import com.livestreaming.streamly.config.utils.AgoraManager
import com.livestreaming.streamly.config.utils.AppCompositionLocals.LocalParentNavController
import com.livestreaming.streamly.config.utils.PermissionUtils
import com.livestreaming.streamly.config.utils.PermissionUtils.corePermissions
import com.livestreaming.streamly.config.utils.SnackbarType
import com.livestreaming.streamly.config.utils.SnackbarUtils
import com.livestreaming.streamly.ui.setup.presentation.component.PermissionsDeniedContent
import com.livestreaming.streamly.ui.setup.presentation.component.PermissionsRequiredContent
import com.livestreaming.streamly.ui.setup.presentation.component.StreamSetupOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(viewModel: SetupViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState = viewModel.uiState.collectAsState()
    val navController = LocalParentNavController.current

    var firstTime = remember { true }
    val agoraManager = remember { AgoraManager(context) }
    var permanentlyDenied by remember { mutableStateOf(false) }
    var permissionsGranted by remember {
        mutableStateOf(PermissionUtils.areAllGranted(context, corePermissions))
    }

    LaunchedEffect(lifecycleOwner) {
        navController?.let { handleEvents(it, viewModel) }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionsGranted = PermissionUtils.areAllGranted(context, corePermissions)
                permanentlyDenied = if (firstTime || permissionsGranted) false else {
                    PermissionUtils.getDeniedPermissions(context, corePermissions).any {
                        PermissionUtils.isPermanentlyDenied(context as Activity, it)
                    }
                }
                firstTime = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            PermissionUtils.handlePermissionResult(
                result = it,
                activity = context as Activity,
                onDenied = { permissionsGranted = false },
                onGranted = { permissionsGranted = true },
                onPermanentlyDenied = { permanentlyDenied = true }
            )
        }
    )

    Scaffold(containerColor = Color.Black) { padding ->
        Box(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .fillMaxWidth()
        ) {
            when {
                permissionsGranted -> {
                    AgoraCameraView(context, lifecycleOwner, agoraManager)

                    StreamSetupOverlay(
                        uiState = uiState.value,
                        onStartClicked = { viewModel.startStream() },
                        onFieldChange = { str, fieldUpdater ->
                            viewModel.onFieldChange(value = str, fieldUpdater = fieldUpdater)
                        }
                    )
                }

                permanentlyDenied -> {
                    PermissionsDeniedContent()
                }

                else -> {
                    PermissionsRequiredContent {
                        PermissionUtils.requestPermissions(corePermissions, launcher)
                    }
                }
            }
        }
    }
}

private suspend fun handleEvents(navController: NavController, viewModel: SetupViewModel) {
    viewModel.events.collect { event ->
        when (event) {
            is SetupEvents.OnError -> {
                SnackbarUtils.show(message = event.error, snackbarType = SnackbarType.Error)
            }

            is SetupEvents.OnStreamStarted -> viewModel.stream?.let {
                navController.navigate(Destination.StreamBroadcast(it.id)) {
                    popUpTo(Destination.StreamSetup) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
}