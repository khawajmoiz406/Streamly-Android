package com.livestreaming.streamly.ui.profile.presentation

sealed class ProfileEvents {
    data object SignedOut : ProfileEvents()
    class OnError(val error: String) : ProfileEvents()
}
