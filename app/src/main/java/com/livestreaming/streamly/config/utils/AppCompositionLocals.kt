package com.livestreaming.streamly.config.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController

object AppCompositionLocals {
    val LocalParentNavController = compositionLocalOf<NavController?> { null }
}