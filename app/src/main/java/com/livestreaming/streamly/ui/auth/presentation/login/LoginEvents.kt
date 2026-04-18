package com.livestreaming.streamly.ui.auth.presentation.login

import com.livestreaming.streamly.core.model.User

sealed class LoginEvents {
    class OnLoginSuccess(val user: User) : LoginEvents()
    class OnLRestLinkSent() : LoginEvents()
    class OnError(val error: String) : LoginEvents()
}