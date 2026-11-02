package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

enum class ThemeMode {
    DARK,
    LIGHT,
    SYSTEM
}

private val DarkColorScheme = darkColorScheme(
    primary = AestheticAccent,
    onPrimary = Color(0xFF0F1015),
    primaryContainer = Color(0xFF2C2A4A),
    onPrimaryContainer = Color(0xFFDEDDFF),
    secondary = AestheticAccentSecondary,
    onSecondary = Color(0xFF00363A),
    secondaryContainer = Color(0xFF1B3B42),
    onSecondaryContainer = Color(0xFFA5F3FC),
    tertiary = AestheticPeach,
    background = S24DarkBackground,
    surface = S24DarkSurface,
    surfaceVariant = S24DarkCard,
    onBackground = S24DarkTextPrimary,
    onSurface = S24DarkTextPrimary,
    onSurfaceVariant = S24DarkTextSecondary,
    outline = S24DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6B63E6),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8E5FF),
    onPrimaryContainer = Color(0xFF1F1B5C),
    secondary = Color(0xFF00838F),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD3F5F7),
    onSecondaryContainer = Color(0xFF00363D),
    tertiary = AestheticPeach,
    background = S24LightBackground,
    surface = S24LightSurface,
    surfaceVariant = S24LightCard,
    onBackground = S24LightTextPrimary,
    onSurface = S24LightTextPrimary,
    onSurfaceVariant = S24LightTextSecondary,
    outline = S24LightBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val aestheticColors = if (darkTheme) DarkPalette else LightPalette
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
        LocalAestheticColors provides aestheticColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
