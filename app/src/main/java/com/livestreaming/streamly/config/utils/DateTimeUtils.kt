package com.livestreaming.streamly.config.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    fun convertMilliToDateTime(milli: Long, pattern: String = "dd MM yyyy HH:mm:ss a"): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(milli))
    }
}