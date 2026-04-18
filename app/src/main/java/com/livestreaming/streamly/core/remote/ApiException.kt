package com.livestreaming.streamly.core.remote

import android.content.Context
import com.livestream.streamly.R

sealed class ApiException(val error: String) : Throwable() {
    data class NetworkException(private val context: Context) :
        ApiException(error = context.getString(R.string.internet_error))

    data class UnknownException(private val context: Context, private val exception: String? = null) :
        ApiException(error = exception ?: context.getString(R.string.unknown_error))
}
