package com.livestreaming.streamly.config.components.layout

import android.content.Context
import android.view.SurfaceView
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.livestreaming.streamly.config.utils.AgoraManager

@Composable
fun AgoraCameraView(
    context: Context,
    isBroadcaster: Boolean,
    lifecycleOwner: LifecycleOwner,
    agoraManager: AgoraManager,
    onJoinSuccess: ((uid: Int) -> Unit)? = null,
    onRemoteUserJoined: ((uid: Int) -> Unit)? = null,
    onRemoteUserLeft: (() -> Unit)? = null,
    onError: ((message: String) -> Unit)? = null,
) {
    val localSurfaceView = remember { SurfaceView(context) }

    LaunchedEffect(lifecycleOwner) {
        agoraManager.apply {
            this.onJoinSuccess = onJoinSuccess
            this.onRemoteUserLeft = onRemoteUserLeft
            this.onError = onError
            this.onRemoteUserJoined = { remoteUid ->
                if (remoteUid == agoraManager.uid) agoraManager.setupRemoteVideo(localSurfaceView)
                onRemoteUserJoined?.invoke(remoteUid)
            }
        }
    }

    if (isBroadcaster) {
        DisposableEffect(lifecycleOwner) {
            agoraManager.initialize()
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> agoraManager.startPreview(localSurfaceView)
                    Lifecycle.Event.ON_PAUSE -> agoraManager.stopPreview()
                    else -> {}
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
                agoraManager.destroy()
            }
        }
    } else {
        DisposableEffect(lifecycleOwner) {
            agoraManager.initialize()
            onDispose { agoraManager.destroy() }
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        @Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { FrameLayout(it).apply { addView(localSurfaceView) } },
        )
    }
}