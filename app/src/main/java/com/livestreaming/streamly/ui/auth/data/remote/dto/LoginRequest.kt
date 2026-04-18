package com.livestreaming.streamly.ui.auth.data.remote.dto

import com.livestreaming.streamly.base.BaseRequest
import com.livestreaming.streamly.core.model.AccountType

data class LoginRequest(
    val email: String?,
    val password: String?,
    val accountType: AccountType,
) : BaseRequest() {
    override fun toMap(): HashMap<String, Any> = hashMapOf()
}