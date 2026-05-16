package com.livestreaming.streamly.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.livestreaming.streamly.config.utils.PictureInPictureUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class PictureInPictureReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            PictureInPictureUtils.ACTION_TOGGLE_MIC -> onToggleMic()
            PictureInPictureUtils.ACTION_MUTE_CAMERA -> onToggleCamera()
        }
    }

    private fun onToggleMic() {
        PipEventBus.emit(PipEvent.ToggleMic)
    }

    private fun onToggleCamera() {
        PipEventBus.emit(PipEvent.ToggleCamera)
    }
}

object PipEventBus {
    private val _events = MutableSharedFlow<PipEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<PipEvent> = _events.asSharedFlow()

    private val _pipState = MutableStateFlow(false)
    val pipState: StateFlow<Boolean> = _pipState.asStateFlow()

    fun emit(event: PipEvent) {
        _events.tryEmit(event)
    }

    fun emitPipState(isInPip: Boolean) {
        _pipState.value = isInPip
    }
}

sealed class PipEvent {
    object ToggleMic : PipEvent()
    object ToggleCamera : PipEvent()
}