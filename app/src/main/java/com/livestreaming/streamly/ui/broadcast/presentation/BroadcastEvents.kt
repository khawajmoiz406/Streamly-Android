package com.livestreaming.streamly.ui.broadcast.presentation

import com.livestreaming.streamly.core.model.Stream

sealed class BroadcastEvents {
    class OnStreamCreated(val stream: Stream?) : BroadcastEvents()
    class OnError(val error: String) : BroadcastEvents()
}