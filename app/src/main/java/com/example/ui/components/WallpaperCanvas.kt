package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.model.ThemeItem
import kotlin.random.Random

@Composable
fun WallpaperCanvas(
    theme: ThemeItem,
    blurRadius: Float = 0f,
    modifier: Modifier = Modifier
) {
    val boxModifier = if (blurRadius > 0f) {
        modifier.blur(blurRadius.dp)
    } else {
        modifier
    }

    Box(modifier = boxModifier.fillMaxSize()) {
        if (theme.imageRes != null) {
            Image(
                painter = painterResource(id = theme.imageRes),
                contentDescription = theme.titleEn,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Procedural aesthetic rendering for themes
            ProceduralWallpaper(theme = theme)
        }
    }
}

@Composable
private fun ProceduralWallpaper(theme: ThemeItem) {
    when (theme.id) {
        "emerald_mist" -> {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val brush = Brush.verticalGradient(
                    colors = if (theme.gradientColors.isNotEmpty()) theme.gradientColors else listOf(Color(0xFF081C15), Color(0xFF1B4332), Color(0xFF0F1E19))
                )
                drawRect(brush)

                // Mist orbs
                drawCircle(
                    color = Color(0x3352B788),
                    radius = size.width * 0.7f,
                    center = Offset(size.width * 0.5f, size.height * 0.65f)
                )
                drawCircle(
                    color = Color(0x22A7F3D0),
                    radius = size.width * 0.5f,
                    center = Offset(size.width * 0.3f, size.height * 0.4f)
                )

                // Silhouette pines along bottom
                val path = Path().apply {
                    moveTo(0f, size.height)
                    lineTo(0f, size.height * 0.84f)
                    var cx = 0f
                    val step = size.width / 8f
                    while (cx <= size.width) {
                        lineTo(cx + step * 0.5f, size.height * 0.76f)
                        lineTo(cx + step, size.height * 0.84f)
                        cx += step
                    }
                    lineTo(size.width, size.height)
                    close()
                }
                drawPath(path, Color(0xFF05110D))
            }
        }
        "sahara_dunes" -> {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val brush = Brush.verticalGradient(
                    colors = if (theme.gradientColors.isNotEmpty()) theme.gradientColors else listOf(Color(0xFF2B1810), Color(0xFFB45309), Color(0xFFF59E0B))
                )
                drawRect(brush)

                // Dune 1
                val d1 = Path().apply {
                    moveTo(0f, size.height * 0.62f)
                    quadraticTo(size.width * 0.45f, size.height * 0.52f, size.width, size.height * 0.70f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(d1, Color(0xDD78350F))

                // Dune 2
                val d2 = Path().apply {
                    moveTo(0f, size.height * 0.78f)
                    quadraticTo(size.width * 0.6f, size.height * 0.70f, size.width, size.height * 0.86f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(d2, Color(0xFF451A03))
            }
        }
        "space_nebula" -> {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Pitch AMOLED Black
                drawRect(Color(0xFF000000))

                // Radial nebula dust
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xBB7928CA), Color(0x664338CA), Color.Transparent),
                        center = Offset(size.width * 0.5f, size.height * 0.45f),
                        radius = size.width * 0.75f
                    ),
                    radius = size.width * 0.75f,
                    center = Offset(size.width * 0.5f, size.height * 0.45f)
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x8806B6D4), Color.Transparent),
                        center = Offset(size.width * 0.75f, size.height * 0.58f),
                        radius = size.width * 0.45f
                    ),
                    radius = size.width * 0.45f,
                    center = Offset(size.width * 0.75f, size.height * 0.58f)
                )

                // Stars
                val rnd = Random(1337)
                for (i in 0 until 120) {
                    val x = rnd.nextFloat() * size.width
                    val y = rnd.nextFloat() * size.height
                    val r = rnd.nextFloat() * 2f + 0.8f
                    drawCircle(Color.White.copy(alpha = 0.85f), r, Offset(x, y))
                }
            }
        }
        "cyber_grid" -> {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val brush = Brush.verticalGradient(
                    colors = if (theme.gradientColors.isNotEmpty()) theme.gradientColors else listOf(Color(0xFF0B0D19), Color(0xFF1E1035), Color(0xFF160826))
                )
                drawRect(brush)

                // Horizon neon glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x88F43F5E), Color(0x338B5CF6), Color.Transparent),
                        center = Offset(size.width * 0.5f, size.height * 0.6f),
                        radius = size.width * 0.6f
                    ),
                    radius = size.width * 0.6f,
                    center = Offset(size.width * 0.5f, size.height * 0.6f)
                )

                val horizonY = size.height * 0.62f
                var y = horizonY
                var step = 15f
                while (y < size.height) {
                    drawLine(
                        color = Color(0x55A855F7),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 2f
                    )
                    y += step
                    step *= 1.25f
                }

                val vpX = size.width * 0.5f
                for (x in -200..(size.width.toInt() + 200) step 80) {
                    drawLine(
                        color = Color(0x44A855F7),
                        start = Offset(vpX, horizonY),
                        end = Offset(x.toFloat(), size.height),
                        strokeWidth = 2f
                    )
                }
            }
        }
        "luxury_obsidian" -> {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val brush = Brush.linearGradient(
                    colors = if (theme.gradientColors.isNotEmpty()) theme.gradientColors else listOf(Color(0xFF090A0D), Color(0xFF14171E), Color(0xFF1F2430), Color(0xFF0B0C10)),
                    start = Offset.Zero,
                    end = Offset(size.width, size.height)
                )
                drawRect(brush)

                // Titanium facets
                var offset = -size.height
                while (offset < size.height * 2f) {
                    drawLine(
                        color = Color(0x14FFFFFF),
                        start = Offset(0f, offset),
                        end = Offset(size.width, offset + size.width * 1.5f),
                        strokeWidth = 2f
                    )
                    offset += 100f
                }
            }
        }
        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = if (theme.gradientColors.isNotEmpty()) theme.gradientColors else listOf(Color(0xFF10121A), Color(0xFF1F2332))
                        )
                    )
            )
        }
    }
}
