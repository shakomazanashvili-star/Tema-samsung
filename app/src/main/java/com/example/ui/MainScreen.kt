package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppLanguage
import com.example.model.LockScreenConfig
import com.example.model.ThemeItem
import com.example.model.ThemeRepository
import com.example.ui.components.ApplyBottomSheet
import com.example.ui.screens.CuratedSetupsScreen
import com.example.ui.screens.CustomizeScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.ThemeGalleryScreen
import com.example.ui.screens.TutorialScreen
import com.example.ui.theme.AestheticAccent
import com.example.ui.theme.S24BorderDark
import com.example.ui.theme.S24CardDark
import com.example.ui.theme.S24SurfaceDark
import com.example.ui.theme.S24TextPrimary
import com.example.ui.theme.S24TextSecondary
import com.example.ui.theme.S24TitaniumBlack
import kotlinx.coroutines.launch

enum class AppNavScreen {
    CURATED,
    GALLERY,
    CUSTOMIZER,
    FAVORITES,
    TUTORIAL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val favoritesViewModel: FavoritesViewModel = viewModel()
    val favorites by favoritesViewModel.favorites.collectAsStateWithLifecycle()
    val filteredFavorites by favoritesViewModel.filteredFavorites.collectAsStateWithLifecycle()
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsStateWithLifecycle()
    val allCategories by favoritesViewModel.allCategories.collectAsStateWithLifecycle()
    val selectedCategory by favoritesViewModel.selectedCategory.collectAsStateWithLifecycle()
    val isGeneratingAi by favoritesViewModel.isGeneratingAi.collectAsStateWithLifecycle()
    val aiStatusMessage by favoritesViewModel.aiStatusMessage.collectAsStateWithLifecycle()

    var language by remember { mutableStateOf(AppLanguage.GEORGIAN) }
    var currentNavScreen by remember { mutableStateOf(AppNavScreen.CURATED) }
    var activeTheme by remember { mutableStateOf(ThemeRepository.themes.first()) }
    var lockScreenConfig by remember {
        mutableStateOf(
            LockScreenConfig(
                clockStyle = ThemeRepository.themes.first().defaultClockStyle,
                clockColor = ThemeRepository.themes.first().clockColor
            )
        )
    }

    var showApplySheet by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Handle back press if inside subscreen
    BackHandler(enabled = currentNavScreen != AppNavScreen.CURATED) {
        currentNavScreen = AppNavScreen.CURATED
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = S24TitaniumBlack,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(AestheticAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Aesthetic S24",
                                color = S24TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Galaxy S24 Ultra • One UI 6.1",
                                color = S24TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                },
                actions = {
                    // Language Switcher Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(S24CardDark)
                            .border(1.dp, S24BorderDark, RoundedCornerShape(16.dp))
                            .clickable {
                                language = if (language == AppLanguage.GEORGIAN) AppLanguage.ENGLISH else AppLanguage.GEORGIAN
                            }
                            .testTag("language_toggle_button")
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = AestheticAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "ქართული" else "English",
                            color = S24TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    IconButton(
                        onClick = { showInfoDialog = true },
                        modifier = Modifier.testTag("info_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "About",
                            tint = S24TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = S24TitaniumBlack,
                    titleContentColor = S24TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = S24SurfaceDark,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentNavScreen == AppNavScreen.CURATED,
                    onClick = { currentNavScreen = AppNavScreen.CURATED },
                    icon = { Icon(Icons.Default.Stars, contentDescription = "Curated") },
                    label = {
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "დიზაინი" else "Setups",
                            fontSize = 10.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AestheticAccent,
                        indicatorColor = AestheticAccent,
                        unselectedIconColor = S24TextSecondary,
                        unselectedTextColor = S24TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_curated_tab")
                )

                NavigationBarItem(
                    selected = currentNavScreen == AppNavScreen.GALLERY,
                    onClick = { currentNavScreen = AppNavScreen.GALLERY },
                    icon = { Icon(Icons.Default.Wallpaper, contentDescription = "Themes") },
                    label = {
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "თემები (9)" else "Themes (9)",
                            fontSize = 10.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AestheticAccent,
                        indicatorColor = AestheticAccent,
                        unselectedIconColor = S24TextSecondary,
                        unselectedTextColor = S24TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_themes_tab")
                )

                NavigationBarItem(
                    selected = currentNavScreen == AppNavScreen.CUSTOMIZER,
                    onClick = { currentNavScreen = AppNavScreen.CUSTOMIZER },
                    icon = { Icon(Icons.Default.Tune, contentDescription = "Customizer") },
                    label = {
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "სტუდია" else "Studio",
                            fontSize = 10.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AestheticAccent,
                        indicatorColor = AestheticAccent,
                        unselectedIconColor = S24TextSecondary,
                        unselectedTextColor = S24TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_studio_tab")
                )

                NavigationBarItem(
                    selected = currentNavScreen == AppNavScreen.FAVORITES,
                    onClick = { currentNavScreen = AppNavScreen.FAVORITES },
                    icon = {
                        if (favoriteIds.isNotEmpty()) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = AestheticAccent,
                                        contentColor = Color.Black
                                    ) {
                                        Text("${favoriteIds.size}")
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Bookmark, contentDescription = "Favorites")
                            }
                        } else {
                            Icon(Icons.Default.Bookmark, contentDescription = "Favorites")
                        }
                    },
                    label = {
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "რჩეული" else "Saved",
                            fontSize = 10.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AestheticAccent,
                        indicatorColor = AestheticAccent,
                        unselectedIconColor = S24TextSecondary,
                        unselectedTextColor = S24TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_favorites_tab")
                )

                NavigationBarItem(
                    selected = currentNavScreen == AppNavScreen.TUTORIAL,
                    onClick = { currentNavScreen = AppNavScreen.TUTORIAL },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "Tutorial") },
                    label = {
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "გზამკვლევი" else "Tutorial",
                            fontSize = 10.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AestheticAccent,
                        indicatorColor = AestheticAccent,
                        unselectedIconColor = S24TextSecondary,
                        unselectedTextColor = S24TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tutorial_tab")
                )
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentNavScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier.padding(innerPadding),
            label = "screen_nav"
        ) { screen ->
            when (screen) {
                AppNavScreen.CURATED -> {
                    CuratedSetupsScreen(
                        language = language,
                        onSelectSetupForCustomizer = { theme, config ->
                            activeTheme = theme
                            lockScreenConfig = config
                            currentNavScreen = AppNavScreen.CUSTOMIZER
                        },
                        onQuickApplySetup = { theme, config ->
                            activeTheme = theme
                            lockScreenConfig = config
                            showApplySheet = true
                        }
                    )
                }
                AppNavScreen.GALLERY -> {
                    ThemeGalleryScreen(
                        language = language,
                        favoriteIds = favoriteIds,
                        onToggleBookmark = { theme ->
                            favoritesViewModel.toggleFavorite(theme, lockScreenConfig)
                        },
                        onSelectThemeForCustomizer = { selected ->
                            activeTheme = selected
                            lockScreenConfig = lockScreenConfig.copy(
                                clockStyle = selected.defaultClockStyle,
                                clockColor = selected.clockColor
                            )
                            currentNavScreen = AppNavScreen.CUSTOMIZER
                        },
                        onQuickApplyTheme = { selected ->
                            activeTheme = selected
                            lockScreenConfig = lockScreenConfig.copy(
                                clockStyle = selected.defaultClockStyle,
                                clockColor = selected.clockColor
                            )
                            showApplySheet = true
                        },
                        onOpenTutorial = {
                            currentNavScreen = AppNavScreen.TUTORIAL
                        }
                    )
                }
                AppNavScreen.CUSTOMIZER -> {
                    CustomizeScreen(
                        theme = activeTheme,
                        config = lockScreenConfig,
                        language = language,
                        onUpdateConfig = { lockScreenConfig = it },
                        onApplyTheme = { showApplySheet = true },
                        onBack = { currentNavScreen = AppNavScreen.CURATED }
                    )
                }
                AppNavScreen.FAVORITES -> {
                    FavoritesScreen(
                        favorites = filteredFavorites,
                        categories = allCategories,
                        selectedCategory = selectedCategory,
                        onSelectCategory = { favoritesViewModel.setCategoryFilter(it) },
                        isGeneratingAi = isGeneratingAi,
                        aiStatusMessage = aiStatusMessage,
                        language = language,
                        onSelectForCustomizer = { theme, config ->
                            activeTheme = theme
                            lockScreenConfig = config
                            currentNavScreen = AppNavScreen.CUSTOMIZER
                        },
                        onQuickApply = { theme, config ->
                            activeTheme = theme
                            lockScreenConfig = config
                            showApplySheet = true
                        },
                        onGenerateAiTags = { theme, config ->
                            favoritesViewModel.generateAiTags(theme, config) { generated ->
                                scope.launch {
                                    snackbarHostState.showSnackbar("Gemini: ${generated.primaryCategory} • ${generated.tags.joinToString(", ")}")
                                }
                            }
                        },
                        onRemoveFavorite = { id ->
                            favoritesViewModel.removeFavorite(id)
                        },
                        onExploreThemes = {
                            currentNavScreen = AppNavScreen.GALLERY
                        }
                    )
                }
                AppNavScreen.TUTORIAL -> {
                    TutorialScreen(language = language)
                }
            }
        }
    }

    // Apply Bottom Sheet
    if (showApplySheet) {
        ApplyBottomSheet(
            theme = activeTheme,
            config = lockScreenConfig,
            language = language,
            sheetState = sheetState,
            onDismiss = { showApplySheet = false },
            onApplied = { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        )
    }

    // Info Dialog
    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            containerColor = S24SurfaceDark,
            title = {
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "Samsung Galaxy S24 Ultra თავსებადობა" else "Samsung S24 Ultra Compatibility",
                    color = S24TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (language == AppLanguage.GEORGIAN)
                            "• შექმნილია iThemeDesign-ის 9 ხელწერის თემის ბაზაზე.\n• QHD+ (1440x3120) და 19.5:9 ფორმატი მორგებულია S24 Ultra-ს ბრტყელ ეკრანზე.\n• მხარდაჭერილია One UI 6.1 Lock Screen ვიჯეტები და AOD ფონის რეჟიმი.\n• ფონის დაყენება შესაძლებელია პირდაპირ აპლიკაციიდან (Lock, Home, Both) ან გალერეაში შენახვით."
                        else
                            "• Built based on iThemeDesign 9 signature aesthetic packs.\n• QHD+ (1440x3120) and 19.5:9 ratio tailored for S24 Ultra's flat display.\n• Supports One UI 6.1 lockscreen widgets and Always On Display wallpaper.\n• Apply directly from the app (Lock, Home, Both) or export to gallery in full resolution.",
                        color = S24TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "გასაგებია" else "Got it",
                        color = AestheticAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
}
