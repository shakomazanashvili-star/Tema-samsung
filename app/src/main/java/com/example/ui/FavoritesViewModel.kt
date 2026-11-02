package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeneratedThemeTags
import com.example.ai.WallpaperTagGenerator
import com.example.data.AppDatabase
import com.example.data.FavoriteRepository
import com.example.data.FavoriteThemeEntity
import com.example.data.FavoriteWallpaperSetEntity
import com.example.model.LockScreenConfig
import com.example.model.ThemeItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FavoriteRepository

    val favorites: StateFlow<List<FavoriteThemeEntity>>
    val favoriteIds: StateFlow<Set<String>>
    val allCategories: StateFlow<List<String>>

    // Favorite Wallpaper Sets (Quick Access)
    val favoriteWallpaperSets: StateFlow<List<FavoriteWallpaperSetEntity>>
    val quickAccessWallpaperSets: StateFlow<List<FavoriteWallpaperSetEntity>>
    val favoriteWallpaperIds: StateFlow<Set<String>>

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    val filteredFavorites: StateFlow<List<FavoriteThemeEntity>>

    private val _isGeneratingAi = MutableStateFlow(false)
    val isGeneratingAi: StateFlow<Boolean> = _isGeneratingAi.asStateFlow()

    private val _aiStatusMessage = MutableStateFlow<String?>(null)
    val aiStatusMessage: StateFlow<String?> = _aiStatusMessage.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = FavoriteRepository(
            dao = database.favoriteThemeDao(),
            wallpaperSetDao = database.favoriteWallpaperSetDao()
        )

        favorites = repository.allFavorites
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        favoriteIds = repository.allFavorites
            .map { list -> list.map { it.themeId }.toSet() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptySet()
            )

        allCategories = repository.allCategories
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        filteredFavorites = combine(repository.allFavorites, _selectedCategory) { list, cat ->
            if (cat.isNullOrBlank()) {
                list
            } else {
                list.filter { it.primaryCategory.equals(cat, ignoreCase = true) }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Wallpaper Sets Flows
        favoriteWallpaperSets = repository.allWallpaperSets
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        quickAccessWallpaperSets = repository.quickAccessWallpaperSets
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        favoriteWallpaperIds = repository.allWallpaperSets
            .map { list -> list.map { it.id }.toSet() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptySet()
            )
    }

    fun setCategoryFilter(category: String?) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(
        theme: ThemeItem,
        config: LockScreenConfig,
        customCategory: String? = null,
        customTags: List<String>? = null,
        vibeDescription: String? = null
    ) {
        val isFav = favoriteIds.value.contains(theme.id)
        viewModelScope.launch {
            if (isFav) {
                repository.removeFavorite(theme.id)
            } else {
                repository.saveFavorite(theme, config, customCategory, customTags, vibeDescription)
            }
        }
    }

    /**
     * Dedicated UI toggle to allow users to save their favorite wallpaper sets for quick access.
     */
    fun toggleFavoriteWallpaperSet(theme: ThemeItem) {
        viewModelScope.launch {
            repository.toggleWallpaperSetFavorite(theme)
        }
    }

    fun toggleQuickAccess(id: String, enabled: Boolean) {
        viewModelScope.launch {
            repository.toggleQuickAccess(id, enabled)
        }
    }

    fun removeWallpaperSet(id: String) {
        viewModelScope.launch {
            repository.removeWallpaperSet(id)
        }
    }

    fun generateAiTags(
        theme: ThemeItem,
        config: LockScreenConfig,
        onResult: (GeneratedThemeTags) -> Unit
    ) {
        viewModelScope.launch {
            _isGeneratingAi.value = true
            _aiStatusMessage.value = "Gemini is analyzing S24 Ultra wallpaper..."
            val result = WallpaperTagGenerator.generateTagsForTheme(theme)
            _isGeneratingAi.value = false

            result.onSuccess { generated ->
                _aiStatusMessage.value = "Generated category: ${generated.primaryCategory}"
                repository.saveFavorite(
                    theme = theme,
                    config = config,
                    customCategory = generated.primaryCategory,
                    customTags = generated.tags,
                    vibeDescription = generated.vibeDescription
                )
                onResult(generated)
            }.onFailure { err ->
                _aiStatusMessage.value = err.message ?: "AI Generation failed"
            }
        }
    }

    fun removeFavorite(themeId: String) {
        viewModelScope.launch {
            repository.removeFavorite(themeId)
        }
    }
}
