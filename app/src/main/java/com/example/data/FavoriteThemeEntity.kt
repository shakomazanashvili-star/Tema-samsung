package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_themes")
data class FavoriteThemeEntity(
    @PrimaryKey val themeId: String,
    val title: String,
    val subtitle: String,
    val clockStyleName: String,
    val clockColorLong: Long,
    val activeWidgetsCsv: String,
    val homeScreenBlur: Float,
    val isVignetteActive: Boolean,
    val primaryCategory: String = "Minimalist",
    val generatedTagsCsv: String = "",
    val vibeDescription: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
