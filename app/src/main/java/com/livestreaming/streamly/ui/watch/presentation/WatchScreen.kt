package com.livestreaming.streamly.ui.watch.presentation

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.layout.AgoraCameraView
import com.livestreaming.streamly.config.components.layout.ConfirmationDialog
import com.livestreaming.streamly.config.components.state.KeepScreenOn
import com.livestreaming.streamly.config.utils.AgoraManager
import com.livestreaming.streamly.config.utils.AppCompositionLocals.LocalParentNavController
import com.livestreaming.streamly.config.utils.AppUtils
import com.livestreaming.streamly.config.utils.PictureInPictureUtils
import com.livestreaming.streamly.config.utils.SnackbarType
import com.livestreaming.streamly.config.utils.SnackbarUtils
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.model.StreamStatus
import com.livestreaming.streamly.receiver.PipEvent
import com.livestreaming.streamly.receiver.PipEventBus
import com.livestreaming.streamly.service.StreamForegroundService
import com.livestreaming.streamly.ui.watch.presentation.component.CameraDisabledContent
import com.livestreaming.streamly.ui.watch.presentation.component.WatchBottomOverlay
import com.livestreaming.streamly.ui.watch.presentation.component.WatchTopOverlay
import kotlinx.coroutines.launch

@SuppressLint("LocalContextGetResourceValueCall", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchScreen(streamId: String, viewModel: WatchViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState = viewModel.uiState.collectAsState()
    val streamState = viewModel.stream.collectAsState()
    val comments = viewModel.comments.collectAsState()
    val navController = LocalParentNavController.current
    val isInPipMode = PipEventBus.pipState.collectAsState()

    val agoraManager = remember { AgoraManager(context) }
    val serviceStarted = remember { mutableStateOf(false) }
    val currentUser = remember { AppUtils.getCurrentUser(context)!! }
    var streamLeftIntentionally by remember { mutableStateOf(false) }

    KeepScreenOn()

    LaunchedEffect(lifecycleOwner) {
        launch { viewModel.getStream(streamId) }
        launch { handleEvents(viewModel, agoraManager) }
        launch {
            handlePipEventBus {
                streamLeftIntentionally = true
                navController?.popBackStack()
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    if (!streamLeftIntentionally && streamState.value != null && streamState.value?.getStreamStatus() == StreamStatus.Live && !isInPipMode.value)
                        enablePipMode(activity, navController, streamState, serviceStarted)
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            if (isInPipMode.value) return@onDispose
            if (streamState.value?.getStreamStatus() != StreamStatus.Ended) {
                agoraManager.leaveAsViewer()
                viewModel.leaveStream(currentUser)
            }
            StreamForegroundService.stop(context)
        }
    }

    BackHandler {
        streamLeftIntentionally = true
        navController?.popBackStack()
    }

    Scaffold { _ ->
        Box(modifier = Modifier.fillMaxWidth()) {
            AgoraCameraView(
                context = context,
                isBroadcaster = false,
                isInPipMode = isInPipMode.value,
                lifecycleOwner = lifecycleOwner,
                agoraManager = agoraManager,
                onError = { SnackbarUtils.show(it, snackbarType = SnackbarType.Error) },
                onJoinSuccess = {
                    viewModel.userJoinedStream(currentUser)
                    serviceStarted.value = true
                    StreamForegroundService.start(
                        context = activity!!,
                        isBroadcaster = false,
                        stream = streamState.value!!,
                        user = currentUser,
                    )
                },
            )

            if (!uiState.value.isLoading && streamState.value != null) {
                if (streamState.value?.cameraOff == true)
                    CameraDisabledContent(isInPipMode.value)

                WatchTopOverlay(
                    isInPipMode = isInPipMode.value,
                    title = streamState.value?.title ?: "",
                    muted = streamState.value?.muted ?: false,
                    hostPhoto = streamState.value?.hostPhotoUrl,
                    hostName = streamState.value?.hostName ?: "",
                    status = streamState.value?.getStreamStatus(),
                    viewers = streamState.value?.viewerCount ?: 0,
                    onBackPressed = {
                        streamLeftIntentionally = true
                        navController?.popBackStack()
                    }
                )

                if (!isInPipMode.value) WatchBottomOverlay(
                    uiState = uiState.value,
                    comments = comments.value,
                    onSendClicked = { viewModel.addCommentToStream(currentUser) },
                    onFieldChange = { str, fieldUpdater ->
                        viewModel.onFieldChange(value = str, fieldUpdater = fieldUpdater)
                    }
                )
            }

            if (streamState.value?.getStreamStatus() == StreamStatus.Ended) Dialog(
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
                onDismissRequest = {},
            ) {
                ConfirmationDialog(
                    title = stringResource(R.string.stream_ended),
                    description = "${streamState.value?.hostName} ${stringResource(R.string.stream_ended_desc)}",
                    positiveButtonLabel = stringResource(R.string.browse_streams),
                    positionClick = {
                        streamLeftIntentionally = true
                        navController?.popBackStack()
                    }
                )
            }
        }
    }
}

private suspend fun handleEvents(viewModel: WatchViewModel, agoraManager: AgoraManager) {
    viewModel.events.collect { event ->
        when (event) {
            is WatchEvents.OnStreamFoundFromFB -> event.stream?.let {
                viewModel.startObservingStream(it.id)
                agoraManager.joinAsViewer(it.agoraChannelId)
            }

            is WatchEvents.OnError -> {
                SnackbarUtils.show(message = event.error, snackbarType = SnackbarType.Error)
            }
        }
    }
}

private suspend fun handlePipEventBus(onLeaveEvent: () -> Unit) {
    PipEventBus.events.collect { event ->
        when (event) {
            is PipEvent.LeaveChannel -> onLeaveEvent.invoke()
            else -> {}
        }
    }
}

private fun enablePipMode(
    activity: Activity?,
    navController: NavController?,
    streamState: State<Stream?>,
    serviceStarted: State<Boolean>
) {
    if (serviceStarted.value) {
        PictureInPictureUtils.enterPipMode(
            activity = activity!!,
            isMuted = streamState.value?.muted ?: false,
            isCameraMute = streamState.value?.cameraOff ?: false,
            isBroadcaster = false
        )
    } else {
        navController?.popBackStack()
    }
}