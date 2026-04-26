package com.livestreaming.streamly.core.model

import androidx.compose.ui.graphics.Color
import java.io.Serializable

data class NavigationItem(
    val name: Int,
    val route: Any,
    val icon: String,
) : Serializable