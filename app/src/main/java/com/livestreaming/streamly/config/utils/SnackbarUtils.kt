package com.livestreaming.streamly.config.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SnackbarUtils {
    private var snackbarHostState: SnackbarHostState? = null
    private var scope: CoroutineScope? = null

    fun init(hostState: SnackbarHostState, coroutineScope: CoroutineScope) {
        snackbarHostState = hostState
        scope = coroutineScope
    }

    fun show(message: String, actionLabel: String? = null) {
        scope?.launch {
            snackbarHostState?.showSnackbar(message = message, actionLabel = actionLabel)
        }
    }

    @Composable
    fun CustomSnackbarHost(hostState: SnackbarHostState) {
        SnackbarHost(hostState = hostState) { data ->
            Snackbar(
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.sdp),
                modifier = Modifier.padding(10.sdp)
            ) {
                Text(
                    text = data.visuals.message,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}