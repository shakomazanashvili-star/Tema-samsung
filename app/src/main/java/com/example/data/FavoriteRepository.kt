package com.example.data

import androidx.compose.ui.graphics.toArgb
import com.example.model.LockScreenConfig
import com.example.model.ThemeItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class FavoriteRepository(
    private val dao: FavoriteThemeDao,
    private val wallpaperSetDao: FavoriteWallpaperSetDao? = null
) {

    val allFavorites: Flow<List<FavoriteThemeEntity>> = dao.getAllFavorites()

    val allCategories: Flow<List<String>> = dao.getAllCategories()

    val allWallpaperSets: Flow<List<FavoriteWallpaperSetEntity>> =
        wallpaperSetDao?.getAllWallpaperSets() ?: emptyFlow()

    val quickAccessWallpaperSets: Flow<List<FavoriteWallpaperSetEntity>> =
        wallpaperSetDao?.getQuickAccessWallpaperSets() ?: emptyFlow()

    fun isFavorite(themeId: String): Flow<Boolean> = dao.isFavorite(themeId)

    fun isWallpaperSetFavorite(id: String): Flow<Boolean> =
        wallpaperSetDao?.isFavoriteWallpaperSet(id) ?: flowOf(false)

    fun getFavoritesByCategory(category: String): Flow<List<FavoriteThemeEntity>> =
        dao.getFavoritesByCategory(category)

    suspend fun saveFavorite(
        theme: ThemeItem,
        config: LockScreenConfig,
        customCategory: String? = null,
        customTags: List<String>? = null,
        vibeDescription: String? = null
    ) {
        val category = customCategory ?: theme.category.name.replace("_", " ").lowercase()
            .replaceFirstChar { it.uppercase() }
        val tagsCsv = customTags?.joinToString(",") ?: theme.tags.joinToString(",")

        val entity = FavoriteThemeEntity(
            themeId = theme.id,
            title = theme.titleEn,
            subtitle = theme.subtitleEn,
            clockStyleName = config.clockStyle.name,
            clockColorLong = config.clockColor.toArgb().toLong(),
            activeWidgetsCsv = config.activeWidgets.joinToString(",") { it.name },
            homeScreenBlur = config.homeScreenBlur,
            isVignetteActive = config.isVignetteActive,
            primaryCategory = category,
            generatedTagsCsv = tagsCsv,
            vibeDescription = vibeDescription ?: "Custom S24 Ultra Lock Screen Setup"
        )
        dao.insertFavorite(entity)

        // Also sync to wallpaper sets for quick access
        saveWallpaperSet(theme, isQuickAccess = true)
    }

    suspend fun saveWallpaperSet(theme: ThemeItem, isQuickAccess: Boolean = true) {
        val hex = "#" + Integer.toHexString(theme.accentColor.toArgb())
        val entity = FavoriteWallpaperSetEntity(
            id = theme.id,
            title = theme.titleEn,
            titleGe = theme.titleGe,
            category = theme.category.name,
            lockscreenResId = theme.imageRes ?: 0,
            homescreenResId = theme.imageRes ?: 0,
            accentColorHex = hex,
            isQuickAccess = isQuickAccess,
            savedAtTimestamp = System.currentTimeMillis(),
            tagsCsv = theme.tags.joinToString(",")
        )
        wallpaperSetDao?.insertWallpaperSet(entity)
    }

    suspend fun removeWallpaperSet(id: String) {
        wallpaperSetDao?.deleteWallpaperSetById(id)
    }

    suspend fun toggleQuickAccess(id: String, isQuickAccess: Boolean) {
        wallpaperSetDao?.updateQuickAccess(id, isQuickAccess)
    }

    suspend fun toggleWallpaperSetFavorite(theme: ThemeItem): Boolean {
        val exists = wallpaperSetDao?.isFavoriteWallpaperSetSync(theme.id) ?: false
        if (exists) {
            wallpaperSetDao?.deleteWallpaperSetById(theme.id)
            return false
        } else {
            saveWallpaperSet(theme, isQuickAccess = true)
            return true
        }
    }

    suspend fun updateAiTags(themeId: String, category: String, tags: List<String>, vibe: String) {
        dao.updateAiMetadata(themeId, category, tags.joinToString(","), vibe)
    }

    suspend fun removeFavorite(themeId: String) {
        dao.deleteFavoriteById(themeId)
        wallpaperSetDao?.deleteWallpaperSetById(themeId)
    }

    suspend fun toggleFavorite(theme: ThemeItem, config: LockScreenConfig, isCurrentlyFav: Boolean) {
        if (isCurrentlyFav) {
            removeFavorite(theme.id)
        } else {
            saveFavorite(theme, config)
        }
    }
}
