package com.livestreaming.streamly.ui.auth.presentation.register

import com.livestreaming.streamly.core.model.User

sealed class RegisterEvents {
    class OnRegisterSuccess(val user: User) : RegisterEvents()
    class OnError(val error: String) : RegisterEvents()
}