package com.livestreaming.streamly.config.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    fun show(
        message: String,
        actionLabel: String? = null,
        snackbarType: SnackbarType? = null,
    ) {
        scope?.launch {
            snackbarHostState?.showSnackbar(
                CustomSnackbarVisuals(
                    message = message,
                    actionLabel = actionLabel,
                    snackbarType = snackbarType,
                )
            )
        }
    }

    @Composable
    fun CustomSnackbarHost(hostState: SnackbarHostState) {
        SnackbarHost(hostState = hostState) { data ->
            val visuals = data.visuals as? CustomSnackbarVisuals
            Snackbar(
                containerColor = getSnackbarColor(visuals?.snackbarType),
                shape = RoundedCornerShape(8.sdp),
                modifier = Modifier.padding(10.sdp)
            ) {
                Text(
                    text = data.visuals.message,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            }
        }
    }

    @Composable
    private fun getSnackbarColor(snackbarType: SnackbarType?): Color = when (snackbarType) {
        SnackbarType.Error -> MaterialTheme.colorScheme.errorContainer
        SnackbarType.Warning -> Color.Yellow
        else -> MaterialTheme.colorScheme.primary
    }
}

enum class SnackbarType { Success, Error, Warning }

data class CustomSnackbarVisuals(
    override val message: String,
    val snackbarType: SnackbarType? = null,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
) : SnackbarVisuals

