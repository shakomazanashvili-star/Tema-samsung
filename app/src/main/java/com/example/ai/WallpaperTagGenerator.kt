package com.example.ai

import android.util.Log
import com.example.model.ThemeItem
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

private const val TAG = "WallpaperTagGenerator"

data class GeneratedThemeTags(
    val primaryCategory: String,
    val tags: List<String>,
    val vibeDescription: String
)

object WallpaperTagGenerator {

    private val tagSchema = Schema.obj(
        properties = mapOf(
            "primaryCategory" to Schema.string(
                description = "Primary visual category, e.g., 'Minimalist', 'Neon Night', 'Abstract Nature', 'AMOLED Dark', 'Cozy Lo-Fi', 'Luxury Obsidian', 'Cyberpunk'"
            ),
            "tags" to Schema.array(
                items = Schema.string(),
                description = "List of 3 to 5 descriptive aesthetic tags like 'True Black 0%', 'Neon Glow', 'Sandstone', 'Rain Reflections'"
            ),
            "vibeDescription" to Schema.string(
                description = "Concise aesthetic explanation of how this wallpaper compliments the Samsung S24 Ultra OLED display"
            )
        )
    )

    private val generativeConfig = generationConfig {
        responseMimeType = "application/json"
        responseSchema = tagSchema
    }

    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generativeConfig
        )
    }

    suspend fun generateTagsForTheme(theme: ThemeItem): Result<GeneratedThemeTags> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Analyze this aesthetic phone wallpaper set designed for Samsung Galaxy S24 Ultra's Dynamic AMOLED 2X display:
                Theme Title: ${theme.titleEn} (${theme.titleGe})
                Description: ${theme.subtitleEn}
                Default Category: ${theme.category.name}
                Current Tags: ${theme.tags.joinToString(", ")}
                Accent Color: ${theme.accentColor}

                Generate a refined primary category (e.g., 'Minimalist', 'Neon Night', 'Abstract Nature', 'AMOLED Dark', 'Cozy Lo-Fi') and 3-5 high-fashion aesthetic tags, plus a 1-sentence vibe description.
            """.trimIndent()

            val response = withTimeout(60_000L) {
                model.generateContent(prompt)
            }

            val jsonString = response.text
                ?: throw IllegalStateException("Gemini API returned an empty response.")

            val json = JSONObject(jsonString)
            val primaryCategory = json.optString("primaryCategory", "Minimalist")
            val tagsArray = json.optJSONArray("tags")
            val tags = mutableListOf<String>()
            if (tagsArray != null) {
                for (i in 0 until tagsArray.length()) {
                    val tag = tagsArray.optString(i)
                    if (tag.isNotBlank()) tags.add(tag)
                }
            }
            if (tags.isEmpty()) {
                tags.addAll(listOf("Aesthetic", "S24 Ultra", "AMOLED"))
            }
            val vibeDescription = json.optString(
                "vibeDescription",
                "High-contrast aesthetic wallpaper paired for the Samsung Galaxy S24 Ultra display."
            )

            Result.success(GeneratedThemeTags(primaryCategory, tags, vibeDescription))
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Gemini tag generation timed out after 60s", e)
            Result.failure(Exception("AI Tag generation timed out. Please try again."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Gemini tag generation failed", e)
            Result.failure(e)
        }
    }
}
