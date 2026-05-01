package com.livestreaming.streamly.config.utils.extension

fun String.getInitials(take: Int = 2): String {
    return this
        .trim()
        .split("\\s+".toRegex())
        .filter { it.isNotEmpty() }
        .take(take)
        .map { it[0].uppercaseChar() }
        .joinToString("")
}