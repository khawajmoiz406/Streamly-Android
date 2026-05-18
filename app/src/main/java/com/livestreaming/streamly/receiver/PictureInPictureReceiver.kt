package com.livestreaming.streamly.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.livestreaming.streamly.MainActivity
import com.livestreaming.streamly.config.utils.PictureInPictureUtils
import com.livestreaming.streamly.core.di.ActivityHolder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class PictureInPictureReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            PictureInPictureUtils.ACTION_TOGGLE_MIC -> PipEventBus.emit(PipEvent.ToggleMic)
            PictureInPictureUtils.ACTION_MUTE_CAMERA -> PipEventBus.emit(PipEvent.ToggleCamera)
            PictureInPictureUtils.ACTION_LEAVE_CHANNEL -> stopEvent(context, PipEvent.LeaveChannel)
            PictureInPictureUtils.ACTION_END_STREAM -> stopEvent(context, PipEvent.EndStream)
        }
    }

    private fun stopEvent(context: Context?, event: PipEvent) {
        ActivityHolder.get()?.let { activity ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && activity.isInPictureInPictureMode) {
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or
                            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                }
                context?.startActivity(intent)
                PipEventBus.emit(event)
            }
        }

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
    object LeaveChannel : PipEvent()
    object EndStream : PipEvent()
}