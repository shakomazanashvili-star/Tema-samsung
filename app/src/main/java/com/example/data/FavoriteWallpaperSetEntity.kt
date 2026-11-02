package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_wallpaper_sets")
data class FavoriteWallpaperSetEntity(
    @PrimaryKey val id: String,
    val title: String,
    val titleGe: String,
    val category: String,
    val lockscreenResId: Int,
    val homescreenResId: Int,
    val accentColorHex: String,
    val isQuickAccess: Boolean = true,
    val savedAtTimestamp: Long = System.currentTimeMillis(),
    val tagsCsv: String = ""
)
