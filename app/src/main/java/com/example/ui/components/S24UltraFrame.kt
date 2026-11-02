package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.LockScreenConfig
import com.example.model.ThemeItem

enum class ScreenPreviewMode {
    LOCK_SCREEN,
    HOME_SCREEN
}

@Composable
fun S24UltraFrame(
    theme: ThemeItem,
    config: LockScreenConfig,
    language: AppLanguage,
    previewMode: ScreenPreviewMode,
    modifier: Modifier = Modifier
) {
    // S24 Ultra has 19.5:9 aspect ratio and iconic sharp corners
    BoxWithConstraints(modifier = modifier) {
        val s24CornerShape = RoundedCornerShape(14.dp)

        Box(
            modifier = Modifier
                .aspectRatio(9f / 19.5f)
                .shadow(elevation = 16.dp, shape = s24CornerShape, spotColor = theme.accentColor.copy(alpha = 0.4f))
                .clip(s24CornerShape)
                // Outer Titanium Frame Border
                .border(
                    width = 2.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFD6CFC7),
                            Color(0xFF8B8884),
                            Color(0xFF4A4846),
                            Color(0xFF8B8884),
                            Color(0xFFD6CFC7)
                        )
                    ),
                    shape = s24CornerShape
                )
                // Black inner screen bezel
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = s24CornerShape
                )
                .background(Color.Black)
        ) {
            // Wallpaper layer
            val blur = if (previewMode == ScreenPreviewMode.HOME_SCREEN) config.homeScreenBlur else 0f
            WallpaperCanvas(
                theme = theme,
                blurRadius = blur,
                modifier = Modifier.fillMaxSize()
            )

            // Dynamic S24 Ultra Status Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Time
                Text(
                    text = "10:30",
                    color = Color.White.copy(alpha = 0.95f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )

                // Icons (5G, WiFi, Battery)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NetworkCell,
                        contentDescription = "5G",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(11.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "WiFi",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = "98%",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.BatteryFull,
                        contentDescription = "Battery",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // Punch Hole Camera (Galaxy S24 Ultra centered front camera)
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF020202))
                    .border(0.5.dp, Color(0xFF1E2028), CircleShape)
            )

            // Screen Content
            if (previewMode == ScreenPreviewMode.LOCK_SCREEN) {
                LockScreenView(
                    theme = theme,
                    config = config,
                    language = language,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                HomeScreenView(
                    theme = theme,
                    language = language,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
