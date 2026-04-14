package com.livestreaming.streamly.config.components.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun <T> rememberDebounce(
    value: T,
    delayMillis: Long = 500L,
    onDebounce: (T) -> Unit
) {
    var isFirstRun by remember { mutableStateOf(true) }

    LaunchedEffect(value) {
        if (isFirstRun) {
            isFirstRun = false
            return@LaunchedEffect
        }
        delay(delayMillis)
        onDebounce(value)
    }
}