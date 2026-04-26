package com.livestreaming.streamly.ui.broadcast.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.livestreaming.streamly.config.components.layout.AgoraCameraView
import com.livestreaming.streamly.config.utils.AgoraManager
import com.livestreaming.streamly.config.utils.AppCompositionLocals.LocalParentNavController
import com.livestreaming.streamly.config.utils.SnackbarType
import com.livestreaming.streamly.config.utils.SnackbarUtils
import com.livestreaming.streamly.ui.broadcast.presentation.component.BroadcastBottomOverlay
import com.livestreaming.streamly.ui.broadcast.presentation.component.BroadcastTopOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BroadcastScreen(streamId: String, viewModel: BroadcastViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState = viewModel.uiState.collectAsState()
    val streamState = viewModel.stream.collectAsState()
    val navController = LocalParentNavController.current
    val agoraManager = remember { AgoraManager(context) }

    LaunchedEffect(lifecycleOwner) {
        viewModel.initialize(streamId)
        agoraManager.goLiveOnChannel(viewModel.stream.value!!.agoraChannelId)
        navController?.let { handleEvents(it, viewModel) }
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .fillMaxWidth()
        ) {
            AgoraCameraView(
                context = context,
                lifecycleOwner = lifecycleOwner,
                agoraManager = agoraManager,
                onJoinSuccess = { viewModel.goLive() },
            )

            BroadcastTopOverlay(
                status = streamState.value?.status,
                viewers = streamState.value?.viewerCount ?: 0
            )

            BroadcastBottomOverlay(
                status = streamState.value?.status,
                isMuted = streamState.value!!.isMuted,
                isCameraDisabled = streamState.value!!.isCameraOff,
                isEndingStream = uiState.value.isEndingLiveStream,
                onMicrophoneClicked = { viewModel.toggleMicrophone() },
                onCameraClicked = { viewModel.toggleCameraOnOff() },
                onPlayPauseClicked = { viewModel.togglePlayPause() },
                onChangeCameraClicked = { agoraManager.switchCamera() },
                onEndClicked = { viewModel.endStream() }
            )
        }
    }
}

private suspend fun handleEvents(navController: NavController, viewModel: BroadcastViewModel) {
    viewModel.events.collect { event ->
        when (event) {
            is BroadcastEvents.OnLiveStreamEndedInFB -> {}

            is BroadcastEvents.OnError -> {
                SnackbarUtils.show(message = event.error, snackbarType = SnackbarType.Error)
            }
        }
    }
}