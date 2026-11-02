package com.example.util

import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.toArgb
import com.example.model.LockScreenConfig
import com.example.model.ThemeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

enum class WallpaperTarget {
    LOCK_SCREEN,
    HOME_SCREEN,
    BOTH
}

object WallpaperHelper {

    private const val S24_ULTRA_WIDTH = 1440
    private const val S24_ULTRA_HEIGHT = 3120

    suspend fun generateWallpaperBitmap(
        context: Context,
        theme: ThemeItem,
        config: LockScreenConfig? = null
    ): Bitmap = withContext(Dispatchers.Default) {
        val baseBitmap = if (theme.imageRes != null) {
            val options = BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
            val original = BitmapFactory.decodeResource(context.resources, theme.imageRes, options)
            if (original != null) {
                Bitmap.createScaledBitmap(original, S24_ULTRA_WIDTH, S24_ULTRA_HEIGHT, true)
            } else {
                renderProceduralTheme(theme)
            }
        } else {
            renderProceduralTheme(theme)
        }

        // Apply filters if config provided
        if (config != null && (config.brightnessAdjustment != 0f || config.contrastAdjustment != 1f || config.isVignetteActive)) {
            applyBitmapFilters(baseBitmap, config)
        } else {
            baseBitmap
        }
    }

    private fun renderProceduralTheme(theme: ThemeItem): Bitmap {
        val bitmap = Bitmap.createBitmap(S24_ULTRA_WIDTH, S24_ULTRA_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val colors = if (theme.gradientColors.isNotEmpty()) {
            theme.gradientColors.map { it.toArgb() }.toIntArray()
        } else {
            intArrayOf(0xFF0F1015.toInt(), 0xFF1E2230.toInt(), 0xFF0A0B0E.toInt())
        }

        when (theme.id) {
            "emerald_mist" -> {
                // Lush vertical gradient
                val gradient = LinearGradient(
                    0f, 0f, 0f, S24_ULTRA_HEIGHT.toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = gradient
                canvas.drawRect(0f, 0f, S24_ULTRA_WIDTH.toFloat(), S24_ULTRA_HEIGHT.toFloat(), paint)

                // Draw atmospheric mist layers & organic curves
                val mistPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = 0x2252B788.toInt()
                    style = Paint.Style.FILL
                }
                canvas.drawCircle(S24_ULTRA_WIDTH * 0.5f, S24_ULTRA_HEIGHT * 0.7f, S24_ULTRA_WIDTH * 0.8f, mistPaint)
                mistPaint.color = 0x18A7F3D0.toInt()
                canvas.drawCircle(S24_ULTRA_WIDTH * 0.3f, S24_ULTRA_HEIGHT * 0.45f, S24_ULTRA_WIDTH * 0.6f, mistPaint)

                // Pine tree silhouettes along bottom
                val treePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = 0xFF05110D.toInt()
                    style = Paint.Style.FILL
                }
                val path = android.graphics.Path()
                path.moveTo(0f, S24_ULTRA_HEIGHT.toFloat())
                path.lineTo(0f, S24_ULTRA_HEIGHT * 0.82f)
                var currentX = 0f
                while (currentX <= S24_ULTRA_WIDTH) {
                    val treeHeight = S24_ULTRA_HEIGHT * (0.75f + (Math.sin(currentX.toDouble() * 0.01).toFloat() * 0.05f))
                    path.lineTo(currentX + 60f, treeHeight)
                    path.lineTo(currentX + 120f, S24_ULTRA_HEIGHT * 0.83f)
                    currentX += 120f
                }
                path.lineTo(S24_ULTRA_WIDTH.toFloat(), S24_ULTRA_HEIGHT.toFloat())
                path.close()
                canvas.drawPath(path, treePaint)
            }
            "sahara_dunes" -> {
                val gradient = LinearGradient(
                    0f, 0f, 0f, S24_ULTRA_HEIGHT.toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = gradient
                canvas.drawRect(0f, 0f, S24_ULTRA_WIDTH.toFloat(), S24_ULTRA_HEIGHT.toFloat(), paint)

                // Sculptural dunes
                val dunePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.FILL
                }
                // Dune 1
                dunePaint.color = 0xAA78350F.toInt()
                val d1 = android.graphics.Path().apply {
                    moveTo(0f, S24_ULTRA_HEIGHT * 0.65f)
                    quadTo(S24_ULTRA_WIDTH * 0.45f, S24_ULTRA_HEIGHT * 0.55f, S24_ULTRA_WIDTH.toFloat(), S24_ULTRA_HEIGHT * 0.72f)
                    lineTo(S24_ULTRA_WIDTH.toFloat(), S24_ULTRA_HEIGHT.toFloat())
                    lineTo(0f, S24_ULTRA_HEIGHT.toFloat())
                    close()
                }
                canvas.drawPath(d1, dunePaint)

                // Dune 2
                dunePaint.color = 0xEE451A03.toInt()
                val d2 = android.graphics.Path().apply {
                    moveTo(0f, S24_ULTRA_HEIGHT * 0.80f)
                    quadTo(S24_ULTRA_WIDTH * 0.6f, S24_ULTRA_HEIGHT * 0.73f, S24_ULTRA_WIDTH.toFloat(), S24_ULTRA_HEIGHT * 0.88f)
                    lineTo(S24_ULTRA_WIDTH.toFloat(), S24_ULTRA_HEIGHT.toFloat())
                    lineTo(0f, S24_ULTRA_HEIGHT.toFloat())
                    close()
                }
                canvas.drawPath(d2, dunePaint)
            }
            "space_nebula" -> {
                // Pitch AMOLED Black
                canvas.drawColor(0xFF000000.toInt())
                // Radial cosmic glow
                val nebulaPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    shader = RadialGradient(
                        S24_ULTRA_WIDTH * 0.5f, S24_ULTRA_HEIGHT * 0.42f,
                        S24_ULTRA_WIDTH * 0.7f,
                        intArrayOf(0xAA7928CA.toInt(), 0x664338CA.toInt(), 0x00000000),
                        null,
                        Shader.TileMode.CLAMP
                    )
                }
                canvas.drawCircle(S24_ULTRA_WIDTH * 0.5f, S24_ULTRA_HEIGHT * 0.42f, S24_ULTRA_WIDTH * 0.7f, nebulaPaint)

                // Subtle secondary cyan starlight
                val starDust = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    shader = RadialGradient(
                        S24_ULTRA_WIDTH * 0.75f, S24_ULTRA_HEIGHT * 0.55f,
                        S24_ULTRA_WIDTH * 0.45f,
                        intArrayOf(0x7706B6D4.toInt(), 0x00000000),
                        null,
                        Shader.TileMode.CLAMP
                    )
                }
                canvas.drawCircle(S24_ULTRA_WIDTH * 0.75f, S24_ULTRA_HEIGHT * 0.55f, S24_ULTRA_WIDTH * 0.45f, starDust)

                // Micro stars
                val starPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = 0xCCFFFFFF.toInt()
                }
                val random = java.util.Random(1337)
                for (i in 0 until 180) {
                    val sx = random.nextFloat() * S24_ULTRA_WIDTH
                    val sy = random.nextFloat() * S24_ULTRA_HEIGHT
                    val radius = (random.nextFloat() * 2.5f) + 1f
                    canvas.drawCircle(sx, sy, radius, starPaint)
                }
            }
            "cyber_grid" -> {
                val gradient = LinearGradient(
                    0f, 0f, 0f, S24_ULTRA_HEIGHT.toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = gradient
                canvas.drawRect(0f, 0f, S24_ULTRA_WIDTH.toFloat(), S24_ULTRA_HEIGHT.toFloat(), paint)

                // Neon horizon glow
                val horizonGlow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    shader = RadialGradient(
                        S24_ULTRA_WIDTH * 0.5f, S24_ULTRA_HEIGHT * 0.6f,
                        S24_ULTRA_WIDTH * 0.6f,
                        intArrayOf(0x99F43F5E.toInt(), 0x338B5CF6.toInt(), 0x00000000),
                        null,
                        Shader.TileMode.CLAMP
                    )
                }
                canvas.drawCircle(S24_ULTRA_WIDTH * 0.5f, S24_ULTRA_HEIGHT * 0.6f, S24_ULTRA_WIDTH * 0.6f, horizonGlow)

                // Perspective grid lines
                val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = 0x44A855F7.toInt()
                    strokeWidth = 3f
                    style = Paint.Style.STROKE
                }
                val horizonY = S24_ULTRA_HEIGHT * 0.62f
                // Horizontals
                var y = horizonY
                var step = 20f
                while (y < S24_ULTRA_HEIGHT) {
                    canvas.drawLine(0f, y, S24_ULTRA_WIDTH.toFloat(), y, gridPaint)
                    y += step
                    step *= 1.25f
                }
                // Verticals radiating from center vanishing point
                val vpX = S24_ULTRA_WIDTH * 0.5f
                for (x in -400..(S24_ULTRA_WIDTH + 400) step 140) {
                    canvas.drawLine(vpX, horizonY, x.toFloat(), S24_ULTRA_HEIGHT.toFloat(), gridPaint)
                }
            }
            "luxury_obsidian" -> {
                val gradient = LinearGradient(
                    0f, 0f, S24_ULTRA_WIDTH.toFloat(), S24_ULTRA_HEIGHT.toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = gradient
                canvas.drawRect(0f, 0f, S24_ULTRA_WIDTH.toFloat(), S24_ULTRA_HEIGHT.toFloat(), paint)

                // Sleek brushed metal diagonal facets
                val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = 0x18FFFFFF.toInt()
                    strokeWidth = 2.5f
                    style = Paint.Style.STROKE
                }
                var offset = -S24_ULTRA_HEIGHT.toFloat()
                while (offset < S24_ULTRA_HEIGHT * 2f) {
                    canvas.drawLine(0f, offset, S24_ULTRA_WIDTH.toFloat(), offset + S24_ULTRA_WIDTH * 1.5f, linePaint)
                    offset += 160f
                }
            }
            else -> {
                val gradient = LinearGradient(
                    0f, 0f, 0f, S24_ULTRA_HEIGHT.toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = gradient
                canvas.drawRect(0f, 0f, S24_ULTRA_WIDTH.toFloat(), S24_ULTRA_HEIGHT.toFloat(), paint)
            }
        }

        return bitmap
    }

    private fun applyBitmapFilters(source: Bitmap, config: LockScreenConfig): Bitmap {
        val result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val cm = ColorMatrix()
        // Contrast & Brightness adjustment
        val contrast = config.contrastAdjustment
        val brightness = config.brightnessAdjustment * 255f
        val scale = contrast
        val translate = (-0.5f * contrast + 0.5f) * 255f + brightness

        val matrix = floatArrayOf(
            scale, 0f, 0f, 0f, translate,
            0f, scale, 0f, 0f, translate,
            0f, 0f, scale, 0f, translate,
            0f, 0f, 0f, 1f, 0f
        )
        cm.set(matrix)
        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(source, 0f, 0f, paint)

        // Apply vignette if enabled
        if (config.isVignetteActive) {
            val vignettePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = RadialGradient(
                    source.width * 0.5f, source.height * 0.5f,
                    source.height * 0.55f,
                    intArrayOf(0x00000000, 0x66000000.toInt(), 0xCC000000.toInt()),
                    floatArrayOf(0f, 0.7f, 1f),
                    Shader.TileMode.CLAMP
                )
            }
            canvas.drawRect(0f, 0f, source.width.toFloat(), source.height.toFloat(), vignettePaint)
        }

        return result
    }

    suspend fun applyWallpaper(
        context: Context,
        theme: ThemeItem,
        target: WallpaperTarget,
        config: LockScreenConfig? = null
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val bitmap = generateWallpaperBitmap(context, theme, config)
            val wallpaperManager = WallpaperManager.getInstance(context)

            val flag = when (target) {
                WallpaperTarget.LOCK_SCREEN -> WallpaperManager.FLAG_LOCK
                WallpaperTarget.HOME_SCREEN -> WallpaperManager.FLAG_SYSTEM
                WallpaperTarget.BOTH -> WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, flag)
            } else {
                wallpaperManager.setBitmap(bitmap)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveToGallery(
        context: Context,
        theme: ThemeItem,
        config: LockScreenConfig? = null
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val bitmap = generateWallpaperBitmap(context, theme, config)
            val filename = "Aesthetic_S24_${theme.id}_${System.currentTimeMillis()}.png"
            var outputStream: OutputStream? = null
            var imageUri: Uri? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/AestheticS24")
                }
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (imageUri != null) {
                    outputStream = resolver.openOutputStream(imageUri)
                }
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val folder = File(imagesDir, "AestheticS24").apply { if (!exists()) mkdirs() }
                val imageFile = File(folder, filename)
                outputStream = FileOutputStream(imageFile)
                imageUri = Uri.fromFile(imageFile)
            }

            outputStream?.use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            if (imageUri != null) {
                Result.success(imageUri)
            } else {
                Result.failure(Exception("Failed to create file"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
