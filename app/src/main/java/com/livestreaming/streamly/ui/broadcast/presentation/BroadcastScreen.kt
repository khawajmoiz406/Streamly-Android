package com.livestreaming.streamly.ui.broadcast.presentation

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.livestreaming.streamly.config.components.state.KeepScreenOn
import com.livestreaming.streamly.config.utils.AgoraManager
import com.livestreaming.streamly.config.utils.AppCompositionLocals.LocalParentNavController
import com.livestreaming.streamly.config.utils.AppUtils
import com.livestreaming.streamly.config.utils.PermissionUtils
import com.livestreaming.streamly.config.utils.PermissionUtils.corePermissions
import com.livestreaming.streamly.config.utils.PictureInPictureUtils
import com.livestreaming.streamly.config.utils.SnackbarType
import com.livestreaming.streamly.config.utils.SnackbarUtils
import com.livestreaming.streamly.core.model.StreamStatus
import com.livestreaming.streamly.receiver.PipEvent
import com.livestreaming.streamly.receiver.PipEventBus
import com.livestreaming.streamly.service.StreamForegroundService
import com.livestreaming.streamly.ui.broadcast.presentation.component.BroadcastBottomOverlay
import com.livestreaming.streamly.ui.broadcast.presentation.component.BroadcastTopOverlay
import com.livestreaming.streamly.ui.broadcast.presentation.component.CameraDisabledContent
import com.livestreaming.streamly.ui.broadcast.presentation.component.PermissionsDeniedContent
import com.livestreaming.streamly.ui.broadcast.presentation.component.PermissionsRequiredContent
import com.livestreaming.streamly.ui.broadcast.presentation.component.StreamSetupOverlay
import kotlinx.coroutines.launch

@SuppressLint("LocalContextGetResourceValueCall", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BroadcastScreen(viewModel: BroadcastViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState = viewModel.uiState.collectAsState()
    val streamState = viewModel.stream.collectAsState()
    val comments = viewModel.comments.collectAsState()
    val navController = LocalParentNavController.current
    val isInPipMode = PipEventBus.pipState.collectAsState()

    var firstTime = remember { true }
    val agoraManager = remember { AgoraManager(context) }
    val serviceStarted = remember { mutableStateOf(false) }
    val currentUser = remember { AppUtils.getCurrentUser(context) }
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
                activity = activity!!,
                onDenied = { permissionsGranted = false },
                onGranted = { permissionsGranted = true },
                onPermanentlyDenied = { permanentlyDenied = true }
            )
        }
    )

    KeepScreenOn()

    LaunchedEffect(lifecycleOwner) {
        launch { handleEvents(viewModel, agoraManager) }
        launch { handlePipEventBus(viewModel) }
    }

    LaunchedEffect(streamState.value?.getStreamStatus()) {
        if (!serviceStarted.value && streamState.value?.getStreamStatus() == StreamStatus.Live) {
            serviceStarted.value = true
            StreamForegroundService.start(
                context = activity!!,
                isBroadcaster = true,
                stream = streamState.value!!,
                user = currentUser!!,
            )
        }
    }

    LaunchedEffect(streamState.value?.muted, streamState.value?.cameraOff) {
        if (!isInPipMode.value) return@LaunchedEffect
        val value = streamState.value ?: return@LaunchedEffect
        PictureInPictureUtils.updatePipActions(
            activity = activity!!,
            isMuted = value.muted,
            isCameraMute = value.cameraOff,
            isBroadcaster = true
        )
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val permissions = corePermissions
                permissionsGranted = PermissionUtils.areAllGranted(context, permissions)
                permanentlyDenied = if (firstTime || permissionsGranted) false else {
                    PermissionUtils.getDeniedPermissions(context, permissions).any {
                        PermissionUtils.isPermanentlyDenied(activity!!, it)
                    }
                }
                firstTime = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            if (isInPipMode.value) return@onDispose
            viewModel.endStream()
            agoraManager.stopBroadcast()
            StreamForegroundService.stop(context)
        }
    }

    BackHandler(streamState.value != null && !isInPipMode.value) {
        if (serviceStarted.value) {
            PictureInPictureUtils.enterPipMode(
                activity = activity!!,
                isMuted = streamState.value?.muted ?: false,
                isCameraMute = streamState.value?.cameraOff ?: false,
                isBroadcaster = true
            )
        } else {
            navController?.popBackStack()
        }
    }

    Scaffold { _ ->
        Box(modifier = Modifier.fillMaxWidth()) {
            when {
                permissionsGranted -> {
                    AgoraCameraView(
                        context = context,
                        isBroadcaster = true,
                        isInPipMode = isInPipMode.value,
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
                            CameraDisabledContent(isInPipMode = isInPipMode.value)

                        BroadcastTopOverlay(
                            status = streamState.value?.getStreamStatus(),
                            viewers = streamState.value?.viewerCount ?: 0,
                            muted = streamState.value?.muted ?: false,
                            isInPipMode = isInPipMode.value
                        )

                        BroadcastBottomOverlay(
                            comments = comments.value,
                            isInPipMode = isInPipMode.value,
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

private suspend fun handlePipEventBus(viewModel: BroadcastViewModel) {
    PipEventBus.events.collect { event ->
        when (event) {
            is PipEvent.ToggleMic -> viewModel.toggleMicrophone()
            is PipEvent.ToggleCamera -> viewModel.toggleCameraOnOff()
        }
    }
}