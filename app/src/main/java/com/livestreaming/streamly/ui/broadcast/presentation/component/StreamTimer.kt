package com.livestreaming.streamly.ui.broadcast.presentation.component

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.livestreaming.streamly.core.model.StreamStatus
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.delay

@Composable
fun StreamTimer(status: StreamStatus?, modifier: Modifier = Modifier) {
    var elapsedSeconds by remember { mutableLongStateOf(0L) }
    val isLive = status == StreamStatus.Live

    LaunchedEffect(isLive) {
        if (isLive) {
            while (true) {
                delay(1000L)
                elapsedSeconds++
            }
        }
    }

    Text(
        text = formatDuration(elapsedSeconds),
        color = Color.White,
        fontSize = 10.ssp,
        fontWeight = FontWeight.Medium,
        modifier = modifier
    )
}

@SuppressLint("DefaultLocale")
fun formatDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, secs)
    } else {
        String.format("%02d:%02d", minutes, secs)
    }
}