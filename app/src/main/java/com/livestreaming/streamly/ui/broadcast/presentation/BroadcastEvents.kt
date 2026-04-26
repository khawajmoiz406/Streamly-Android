package com.livestreaming.streamly.ui.broadcast.presentation

sealed class BroadcastEvents {
    class OnLiveStreamEndedInFB() : BroadcastEvents()
    class OnError(val error: String) : BroadcastEvents()
}