package com.livestreaming.streamly.ui.setup.presentation

sealed class SetupEvents {
    class OnStreamStarted() : SetupEvents()
    class OnError(val error: String) : SetupEvents()
}