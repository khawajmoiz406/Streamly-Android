package com.livestreaming.streamly.ui.broadcast.presentation

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.layout.AgoraCameraView
import com.livestreaming.streamly.config.components.layout.ConfirmationDialog
import com.livestreaming.streamly.config.utils.AgoraManager
import com.livestreaming.streamly.config.utils.AppCompositionLocals.LocalParentNavController
import com.livestreaming.streamly.config.utils.AppUtils
import com.livestreaming.streamly.config.utils.PermissionUtils
import com.livestreaming.streamly.config.utils.PermissionUtils.corePermissions
import com.livestreaming.streamly.config.utils.SnackbarType
import com.livestreaming.streamly.config.utils.SnackbarUtils
import com.livestreaming.streamly.ui.broadcast.presentation.component.BroadcastBottomOverlay
import com.livestreaming.streamly.ui.broadcast.presentation.component.BroadcastTopOverlay
import com.livestreaming.streamly.ui.broadcast.presentation.component.CameraDisabledContent
import com.livestreaming.streamly.ui.broadcast.presentation.component.PermissionsDeniedContent
import com.livestreaming.streamly.ui.broadcast.presentation.component.PermissionsRequiredContent
import com.livestreaming.streamly.ui.broadcast.presentation.component.StreamSetupOverlay

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BroadcastScreen(viewModel: BroadcastViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState = viewModel.uiState.collectAsState()
    val streamState = viewModel.stream.collectAsState()
    val comments = viewModel.comments.collectAsState()
    val navController = LocalParentNavController.current

    var firstTime = remember { true }
    val agoraManager = remember { AgoraManager(context) }
    var permanentlyDenied by remember { mutableStateOf(false) }
    var showEndStreamDialog by remember { mutableStateOf(false) }
    var permissionsGranted by remember {
        mutableStateOf(PermissionUtils.areAllGranted(context, corePermissions))
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

    LaunchedEffect(lifecycleOwner) {
        handleEvents(viewModel, agoraManager)
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
        onDispose {
            viewModel.endStream()
            agoraManager.stopBroadcast()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Scaffold { padding ->
        Box(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .fillMaxWidth()
        ) {
            when {
                permissionsGranted -> {
                    AgoraCameraView(
                        context = context,
                        isBroadcaster = true,
                        lifecycleOwner = lifecycleOwner,
                        agoraManager = agoraManager,
                        onJoinSuccess = { viewModel.goLive() },
                        onError = { SnackbarUtils.show(it, snackbarType = SnackbarType.Error) },
                    )

                    if (streamState.value == null) {
                        StreamSetupOverlay(
                            uiState = uiState.value,
                            onStartClicked = { viewModel.startStream(AppUtils.getCurrentUser(context)) },
                            onFieldChange = { str, fieldUpdater ->
                                viewModel.onFieldChange(value = str, fieldUpdater = fieldUpdater)
                            }
                        )
                    } else {
                        if (streamState.value?.cameraOff == true)
                            CameraDisabledContent()

                        BroadcastTopOverlay(
                            status = streamState.value?.getStreamStatus(),
                            viewers = streamState.value?.viewerCount ?: 0,
                            muted = streamState.value?.muted ?: false
                        )

                        BroadcastBottomOverlay(
                            comments = comments.value,
                            title = streamState.value?.title ?: "",
                            status = streamState.value?.getStreamStatus(),
                            isMuted = streamState.value?.muted ?: false,
                            isEndingStream = uiState.value.isEndingLiveStream,
                            isCameraDisabled = streamState.value?.cameraOff ?: false,
                            onChangeCameraClicked = { agoraManager.switchCamera() },
                            onEndClicked = { showEndStreamDialog = true },
                            onCameraClicked = {
                                val newValue = !(streamState.value?.cameraOff ?: true)
                                agoraManager.muteLocalCamera(newValue)
                                viewModel.toggleCameraOnOff()
                            },
                            onMicrophoneClicked = {
                                val newValue = !(streamState.value?.muted ?: true)
                                agoraManager.toggleMic(newValue)
                                viewModel.toggleMicrophone()
                            }
                        )
                    }
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

        if (showEndStreamDialog) {
            Dialog(onDismissRequest = { }) {
                ConfirmationDialog(
                    title = stringResource(R.string.end_stream_title),
                    description = stringResource(R.string.end_stream_msg),
                    positiveButtonLabel = stringResource(R.string.end_stream),
                    negativeButtonLabel = stringResource(R.string.keep_streaming),
                    negativeClick = { showEndStreamDialog = false },
                    positionClick = {
                        showEndStreamDialog = false
                        navController?.popBackStack()
                    },
                )
            }
        }
    }
}

private suspend fun handleEvents(viewModel: BroadcastViewModel, agoraManager: AgoraManager) {
    viewModel.events.collect { event ->
        when (event) {
            is BroadcastEvents.OnStreamCreated -> event.stream?.let {
                viewModel.startObservingStream(it.id)
                agoraManager.goLiveOnChannel(it.agoraChannelId)
            }

            is BroadcastEvents.OnError -> {
                SnackbarUtils.show(message = event.error, snackbarType = SnackbarType.Error)
            }
        }
    }
}