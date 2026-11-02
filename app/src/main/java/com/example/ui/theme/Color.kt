package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Samsung Galaxy S24 Ultra & iThemeDesign Aesthetic Palettes

// S24 Ultra Titanium OLED Dark Palette
val S24DarkBackground = Color(0xFF0F1015)
val S24DarkSurface = Color(0xFF161820)
val S24DarkCard = Color(0xFF1F222E)
val S24DarkBorder = Color(0xFF2E3242)
val S24DarkTextPrimary = Color(0xFFF3F4F6)
val S24DarkTextSecondary = Color(0xFFA1A7B7)
val S24DarkTextMuted = Color(0xFF6B7280)

// S24 Ultra Titanium Light Palette (One UI 6.1 Porcelain & Silver)
val S24LightBackground = Color(0xFFF7F8FA)
val S24LightSurface = Color(0xFFFFFFFF)
val S24LightCard = Color(0xFFEFF1F5)
val S24LightBorder = Color(0xFFDDE0E6)
val S24LightTextPrimary = Color(0xFF13151A)
val S24LightTextSecondary = Color(0xFF5A6170)
val S24LightTextMuted = Color(0xFF8B92A2)

// Signature Aesthetic Accents
val AestheticAccent = Color(0xFF8B85FF)
val AestheticAccentSecondary = Color(0xFF5CE1E6)
val AestheticPeach = Color(0xFFFF9E7D)
val AestheticGold = Color(0xFFF6C876)
val AestheticRose = Color(0xFFFFAAA6)
val AestheticEmerald = Color(0xFF48CFAD)
val S24TitaniumGray = Color(0xFF9EA3B0)

data class AestheticColorPalette(
    val background: Color,
    val surface: Color,
    val card: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val accent: Color = AestheticAccent,
    val accentSecondary: Color = AestheticAccentSecondary,
    val isDark: Boolean = true
)

val DarkPalette = AestheticColorPalette(
    background = S24DarkBackground,
    surface = S24DarkSurface,
    card = S24DarkCard,
    border = S24DarkBorder,
    textPrimary = S24DarkTextPrimary,
    textSecondary = S24DarkTextSecondary,
    textMuted = S24DarkTextMuted,
    accent = AestheticAccent,
    accentSecondary = AestheticAccentSecondary,
    isDark = true
)

val LightPalette = AestheticColorPalette(
    background = S24LightBackground,
    surface = S24LightSurface,
    card = S24LightCard,
    border = S24LightBorder,
    textPrimary = S24LightTextPrimary,
    textSecondary = S24LightTextSecondary,
    textMuted = S24LightTextMuted,
    accent = Color(0xFF6B63E6),
    accentSecondary = Color(0xFF00838F),
    isDark = false
)

val LocalAestheticColors = staticCompositionLocalOf { DarkPalette }

// Composable dynamic accessors ensuring all screens adapt reactively to light & dark theme
val S24TitaniumBlack: Color
    @Composable get() = LocalAestheticColors.current.background

val S24SurfaceDark: Color
    @Composable get() = LocalAestheticColors.current.surface

val S24CardDark: Color
    @Composable get() = LocalAestheticColors.current.card

val S24BorderDark: Color
    @Composable get() = LocalAestheticColors.current.border

val S24TextPrimary: Color
    @Composable get() = LocalAestheticColors.current.textPrimary

val S24TextSecondary: Color
    @Composable get() = LocalAestheticColors.current.textSecondary

val S24TextMuted: Color
    @Composable get() = LocalAestheticColors.current.textMuted
