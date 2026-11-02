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
    repository = FavoriteRepository(database.favoriteThemeDao())
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
}
