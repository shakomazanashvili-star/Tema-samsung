package com.example.data

import androidx.compose.ui.graphics.toArgb
import com.example.model.LockScreenConfig
import com.example.model.ThemeItem
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val dao: FavoriteThemeDao) {

    val allFavorites: Flow<List<FavoriteThemeEntity>> = dao.getAllFavorites()

    val allCategories: Flow<List<String>> = dao.getAllCategories()

    fun isFavorite(themeId: String): Flow<Boolean> = dao.isFavorite(themeId)

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
    }

    suspend fun updateAiTags(themeId: String, category: String, tags: List<String>, vibe: String) {
        dao.updateAiMetadata(themeId, category, tags.joinToString(","), vibe)
    }

    suspend fun removeFavorite(themeId: String) {
        dao.deleteFavoriteById(themeId)
    }

    suspend fun toggleFavorite(theme: ThemeItem, config: LockScreenConfig, isCurrentlyFav: Boolean) {
        if (isCurrentlyFav) {
            removeFavorite(theme.id)
        } else {
            saveFavorite(theme, config)
        }
    }
}
