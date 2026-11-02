package com.example

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun `tutorial data steps contain step imagery, descriptions and routes`() {
    val steps = com.example.model.TutorialData.steps
    assertEquals(5, steps.size)
    steps.forEach { step ->
      assertTrue(step.titleEn.isNotEmpty())
      assertTrue(step.titleGe.isNotEmpty())
      assertTrue(step.shortSummaryEn.isNotEmpty())
      assertTrue(step.instructionsEn.size >= 3)
      assertTrue(step.instructionsGe.size >= 3)
      assertTrue(step.tipEn.isNotEmpty())
      assertTrue(step.tipGe.isNotEmpty())
      assertNotNull(step.imageRes)
      assertNotNull(step.actionRoute)
    }
  }

  @Test
  fun `theme repository contains all 12 themes`() {
    val themes = com.example.model.ThemeRepository.themes
    assertEquals(12, themes.size)
  }
}
