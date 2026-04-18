package com.livestreaming.streamly.ui.auth.data.remote.dto

import android.net.Uri
import com.livestreaming.streamly.base.BaseRequest

data class RegisterRequest(
    val name: String?,
    val email: String?,
    val password: String?,
    val phoneNumber: String?,
    val profilePicture: Uri?,
) : BaseRequest() {
    override fun toMap(): HashMap<String, Any> = hashMapOf()
}