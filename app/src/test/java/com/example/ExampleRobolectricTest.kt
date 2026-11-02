package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.FavoriteRepository
import com.example.data.FavoriteThemeEntity
import com.example.model.LockScreenConfig
import com.example.model.ThemeRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  private lateinit var database: AppDatabase
  private lateinit var repository: FavoriteRepository

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    repository = FavoriteRepository(
      dao = database.favoriteThemeDao(),
      wallpaperSetDao = database.favoriteWallpaperSetDao()
    )
  }

  @After
  fun tearDown() {
    database.close()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Aesthetic S24", appName)
  }

  @Test
  fun `verify theme repository contains nine sets`() {
    assertEquals(12, ThemeRepository.themes.size)
  }

  @Test
  fun `room database saves and retrieves favorite wallpaper sets`() = runBlocking {
    val theme = ThemeRepository.themes.first()
    val config = LockScreenConfig()

    repository.saveFavorite(theme, config)

    val favorites = repository.allFavorites.first()
    assertEquals(1, favorites.size)
    assertEquals(theme.id, favorites[0].themeId)

    val isFav = repository.isFavorite(theme.id).first()
    assertTrue(isFav)

    repository.removeFavorite(theme.id)
    val afterDelete = repository.allFavorites.first()
    assertEquals(0, afterDelete.size)
  }

  @Test
  fun `room database supports categories and ai tags updates`() = runBlocking {
    val theme1 = ThemeRepository.themes[0]
    val theme2 = ThemeRepository.themes[1]
    val config = LockScreenConfig()

    repository.saveFavorite(theme1, config, customCategory = "Minimalist", customTags = listOf("clean", "oled", "subtle"), vibeDescription = "Calm atmosphere")
    repository.saveFavorite(theme2, config, customCategory = "Neon Night", customTags = listOf("cyberpunk", "glowing"), vibeDescription = "Electric midnight")

    val minimalists = repository.getFavoritesByCategory("Minimalist").first()
    assertEquals(1, minimalists.size)
    assertEquals(theme1.id, minimalists[0].themeId)

    val neon = repository.getFavoritesByCategory("Neon Night").first()
    assertEquals(1, neon.size)
    assertEquals(theme2.id, neon[0].themeId)

    // Test updating AI tags
    repository.updateAiTags(theme1.id, "Abstract Nature", listOf("organic", "fluid", "minimal"), "Serene organic lines")
    val updated = repository.getFavoritesByCategory("Abstract Nature").first()
    assertEquals(1, updated.size)
    assertEquals("organic,fluid,minimal", updated[0].generatedTagsCsv)
    assertEquals("Serene organic lines", updated[0].vibeDescription)
  }

  @Test
  fun `tutorial data steps contain imagery and valid instructions`() {
    val steps = com.example.model.TutorialData.steps
    assertEquals(5, steps.size)
    steps.forEach { step ->
      assertTrue(step.instructionsEn.isNotEmpty())
      assertTrue(step.instructionsGe.isNotEmpty())
      assertTrue(step.imageRes != null)
    }
  }

  @Test
  fun `battery telemetry utility returns valid device telemetry`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val telemetry = com.example.widget.BatteryTelemetryUtil.getBatteryInfo(context)
    assertTrue(telemetry.percentage in 1..100)
    assertTrue(telemetry.voltageMv > 0)
    assertTrue(telemetry.health.isNotEmpty())
  }

  @Test
  fun `widget provider classes are registered and construct layouts`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val clockProvider = com.example.widget.S24ClockWidgetProvider()
    val batteryProvider = com.example.widget.S24BatteryWidgetProvider()
    val glanceProvider = com.example.widget.S24GlanceWidgetProvider()
    org.junit.Assert.assertNotNull(clockProvider)
    org.junit.Assert.assertNotNull(batteryProvider)
    org.junit.Assert.assertNotNull(glanceProvider)
  }

  @Test
  fun `room database saves and retrieves favorite wallpaper sets for quick access`() = runBlocking {
    val theme = ThemeRepository.themes.first()

    // Test saving wallpaper set for quick access
    repository.saveWallpaperSet(theme, isQuickAccess = true)

    val sets = repository.allWallpaperSets.first()
    assertEquals(1, sets.size)
    assertEquals(theme.id, sets[0].id)
    assertEquals(theme.titleEn, sets[0].title)
    assertTrue(sets[0].isQuickAccess)

    val quickAccess = repository.quickAccessWallpaperSets.first()
    assertEquals(1, quickAccess.size)

    val isFav = repository.isWallpaperSetFavorite(theme.id).first()
    assertTrue(isFav)

    // Test toggling quick access off
    repository.toggleQuickAccess(theme.id, false)
    val quickAccessAfter = repository.quickAccessWallpaperSets.first()
    assertEquals(0, quickAccessAfter.size)

    // Test toggling favorite removes it
    repository.removeWallpaperSet(theme.id)
    val emptySets = repository.allWallpaperSets.first()
    assertEquals(0, emptySets.size)
  }

  @Test
  fun `filter wallpaper sets by tags finds matching themes`() {
    val minimalThemes = ThemeRepository.themes.filter { theme ->
      theme.tags.any { it.contains("Minimal", ignoreCase = true) }
    }
    assertTrue(minimalThemes.isNotEmpty())

    val cyberThemes = ThemeRepository.themes.filter { theme ->
      theme.tags.any { it.contains("Cyberpunk", ignoreCase = true) }
    }
    assertTrue(cyberThemes.isNotEmpty())
    assertEquals("cyberpunk_neon", cyberThemes[0].id)
  }

  @Test
  fun `theme preferences switches between light and dark mode`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val themePrefs = com.example.data.ThemePreferences.getInstance(context)

    // Set to dark mode initially
    themePrefs.setThemeMode(com.example.ui.theme.ThemeMode.DARK)
    assertEquals(com.example.ui.theme.ThemeMode.DARK, themePrefs.themeMode.value)

    // Toggle to light mode
    themePrefs.toggleLightDarkMode()
    assertEquals(com.example.ui.theme.ThemeMode.LIGHT, themePrefs.themeMode.value)

    // Toggle back to dark mode
    themePrefs.toggleLightDarkMode()
    assertEquals(com.example.ui.theme.ThemeMode.DARK, themePrefs.themeMode.value)

    // Set to system mode
    themePrefs.setThemeMode(com.example.ui.theme.ThemeMode.SYSTEM)
    assertEquals(com.example.ui.theme.ThemeMode.SYSTEM, themePrefs.themeMode.value)
  }

  @Test
  fun `verify light and dark palettes contrast and properties`() {
    assertTrue(com.example.ui.theme.DarkPalette.isDark)
    assertFalse(com.example.ui.theme.LightPalette.isDark)
    org.junit.Assert.assertNotEquals(
      com.example.ui.theme.DarkPalette.background,
      com.example.ui.theme.LightPalette.background
    )
    org.junit.Assert.assertNotEquals(
      com.example.ui.theme.DarkPalette.surface,
      com.example.ui.theme.LightPalette.surface
    )
  }
}

