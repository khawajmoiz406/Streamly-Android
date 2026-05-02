package com.livestreaming.streamly.ui.watch.presentation

import com.livestreaming.streamly.core.model.Stream

sealed class WatchEvents {
    class OnStreamFoundFromFB(val stream: Stream?) : WatchEvents()
    class OnLeaveSuccess() : WatchEvents()
    class OnError(val error: String) : WatchEvents()
}