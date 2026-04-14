package com.livestreaming.streamly.config.theme

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object ThemeState {
    val darkTheme = mutableStateOf(false)
}

sealed class ThemeMode(val value: Int) {
    data object Dark : ThemeMode(1)
    data object Light : ThemeMode(2)
}

private val LightColorScheme = lightColorScheme(
    primary = Green,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0B5C45),
    onPrimaryContainer = Color(0xFFD6FFF4)
)

private val DarkColorScheme = darkColorScheme(
    primary = Green,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0B5C45),
    onPrimaryContainer = Color(0xFFD6FFF4)
)

val ColorScheme.disabledContent: Color
    get() = onSurface.copy(alpha = 0.38f)

val ColorScheme.disabledContainer: Color
    get() = onSurface.copy(alpha = 0.12f)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = ThemeState.darkTheme.value,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
@Preview(showBackground = false)
private fun LightColorSchemePreview() {
    val colors = LightColorScheme

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