package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

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
    background = S24TitaniumBlack,
    surface = S24SurfaceDark,
    surfaceVariant = S24CardDark,
    onBackground = S24TextPrimary,
    onSurface = S24TextPrimary,
    onSurfaceVariant = S24TextSecondary,
    outline = S24BorderDark
)

private val LightColorScheme = darkColorScheme( // We prefer a dark aesthetic by default for S24 Ultra AMOLED
    primary = AestheticAccent,
    background = S24TitaniumBlack,
    surface = S24SurfaceDark,
    surfaceVariant = S24CardDark,
    onBackground = S24TextPrimary,
    onSurface = S24TextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent aesthetic theme
    content: @Composable () -> Unit,
) {
    val colorScheme = DarkColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
