package com.example.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.R

enum class ThemeCategory {
    ALL,
    FAVORITES,
    MINIMALIST,
    AMOLED_DARK,
    CYBERPUNK,
    AESTHETIC_COZY,
    NATURE
}

data class ThemeItem(
    val id: String,
    val titleGe: String,
    val titleEn: String,
    val subtitleGe: String,
    val subtitleEn: String,
    val category: ThemeCategory,
    @DrawableRes val imageRes: Int? = null,
    val gradientColors: List<Color> = emptyList(),
    val accentColor: Color = Color(0xFF8B85FF),
    val clockColor: Color = Color.White,
    val defaultClockStyle: ClockStyle = ClockStyle.SERIF_BOLD,
    val widgetPalette: List<Color> = listOf(Color.White.copy(alpha = 0.85f)),
    val tags: List<String> = emptyList(),
    val isFeatured: Boolean = false
)

enum class ClockStyle {
    SERIF_BOLD,       // Luxury editorial font
    CLEAN_SANS,       // Modern crisp sans-serif
    STACKED_NUMBERS,  // Big stacked digits (HH / MM)
    THIN_ROMAN,       // Refined minimalist thin
    HANDWRITTEN,      // Aesthetic script vibe
    PILL_DIGITAL      // Floating modern pill
}

enum class WidgetType {
    BATTERY_RING,
    WEATHER,
    STEP_COUNTER,
    CALENDAR_EVENT,
    MUSIC_PLAYER,
    QUOTE_TEXT
}

data class LockScreenConfig(
    val clockStyle: ClockStyle = ClockStyle.SERIF_BOLD,
    val clockColor: Color = Color.White,
    val clockSizeMultiplier: Float = 1.0f,
    val activeWidgets: List<WidgetType> = listOf(WidgetType.WEATHER, WidgetType.BATTERY_RING, WidgetType.STEP_COUNTER),
    val showNotificationPill: Boolean = true,
    val showShortcuts: Boolean = true,
    val homeScreenBlur: Float = 0f,
    val brightnessAdjustment: Float = 0f,
    val contrastAdjustment: Float = 1f,
    val isVignetteActive: Boolean = false
)

enum class AppLanguage {
    GEORGIAN,
    ENGLISH
}
