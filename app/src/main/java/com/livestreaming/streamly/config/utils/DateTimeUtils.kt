package com.livestreaming.streamly.config.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    fun convertMilliToDateTime(milli: Long, pattern: String = "dd MM yyyy HH:mm:ss a"): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(milli))
    }

    fun formatDurationShort(ms: Long): String {
        if (ms <= 0) return "0m"
        val totalMinutes = ms / 1000 / 60
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return when {
            hours == 0L -> "${minutes}m"
            minutes == 0L -> "${hours}h"
            else -> "${hours}h ${minutes}m"
        }
    }

    fun formatStreamTimeAgo(timeInMillis: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timeInMillis
        val days = diff / 1000 / 60 / 60 / 24
        return when {
            days <= 0 -> "Today"
            days == 1L -> "Yesterday"
            days < 7 -> "$days days ago"
            else -> calculateTimeAgoFromMilli(timeInMillis)
        }
    }

    fun calculateTimeAgoFromMilli(timeInMillis: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timeInMillis

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        return when {
            seconds < 60 -> "Just now"
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"
            days < 7 -> "${days}d ago"
            weeks < 4 -> "${weeks}w ago"
            months < 12 -> "${months}mo ago"
            else -> "${years}y ago"
        }
    }
}