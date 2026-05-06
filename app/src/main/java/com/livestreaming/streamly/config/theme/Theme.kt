package com.livestreaming.streamly.config.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

object ThemeState {
    val darkTheme = mutableStateOf(true)
}

sealed class ThemeMode(val value: Int) {
    data object Dark : ThemeMode(1)
    data object Light : ThemeMode(2)

    companion object {
        fun fromValue(value: Int): ThemeMode? = when (value) {
            1 -> Dark
            2 -> Light
            else -> null
        }
    }
}

private val LightColorScheme = lightColorScheme(
    primary = Green,
    onPrimary = Color.White,
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFFFFFFF),

    secondary = Orange,
    onSecondary = Color.White,
)
private val DarkColorScheme = darkColorScheme(
    primary = Green,
    onPrimary = Color.White,
    background = Color(0xFF111111),
    surface = Color(0xFF1E1E1E),

    secondary = Orange,
    onSecondary = Color.White,
)

val ColorScheme.disabledContent: Color
    get() = if (ThemeState.darkTheme.value) Color(0xFF616161) else Color(0xFF9E9E9E)

val ColorScheme.disabledContainer: Color
    get() = if (ThemeState.darkTheme.value) Color(0xFF2C2C2C) else Color(0xFFE0E0E0)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = ThemeState.darkTheme.value,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    if(!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
@Preview(showBackground = false)
private fun LightColorSchemePreview() {
    val colors = DarkColorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ColorBox("primary", colors.primary)
        ColorBox("onPrimary", colors.onPrimary)
        ColorBox("primaryContainer", colors.primaryContainer)
        ColorBox("onPrimaryContainer", colors.onPrimaryContainer)
        ColorBox("secondary", colors.secondary)
        ColorBox("onSecondary", colors.onSecondary)
        ColorBox("background", colors.background)
        ColorBox("onBackground", colors.onBackground)
        ColorBox("surface", colors.surface)
        ColorBox("onSurface", colors.onSurface)
        ColorBox("error", colors.error)
        ColorBox("onError", colors.onError)
    }
}

@Composable
private fun ColorBox(label: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = if (color.luminance() < 0.5f) Color.White else Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}