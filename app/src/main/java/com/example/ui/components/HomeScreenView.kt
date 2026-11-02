package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ThemeItem

@Composable
fun HomeScreenView(
    theme: ThemeItem,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val dateText = if (language == AppLanguage.GEORGIAN) "31 ოქტომბერი • თბილისი" else "Oct 31 • Sunny"

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 44.dp, start = 18.dp, end = 18.dp, bottom = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Aesthetic Large Widget (Weather & Clock Pill)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.Black.copy(alpha = 0.38f))
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(22.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "10:30",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = dateText,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = Color(0xFFFFD166),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "22°",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Themed App Icons Grid (4x2 grid of aesthetic minimalist icons)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    AestheticAppIcon("Gallery", Icons.Default.Folder, theme.accentColor)
                    AestheticAppIcon("Camera", Icons.Default.CameraAlt, theme.accentColor)
                    AestheticAppIcon("Spotify", Icons.Default.MusicNote, theme.accentColor)
                    AestheticAppIcon("Browser", Icons.Default.Language, theme.accentColor)
                }

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    AestheticAppIcon("Mail", Icons.Default.Email, theme.accentColor)
                    AestheticAppIcon("Notes", Icons.Default.ChatBubble, theme.accentColor)
                    AestheticAppIcon("Settings", Icons.Default.Settings, theme.accentColor)
                    AestheticAppIcon("Phone", Icons.Default.Phone, theme.accentColor)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // One UI Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black.copy(alpha = 0.42f))
                    .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "ძიება One UI..." else "Search...",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Dock
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AestheticAppIcon("Phone", Icons.Default.Phone, theme.accentColor, showLabel = false)
                AestheticAppIcon("Messages", Icons.Default.ChatBubble, theme.accentColor, showLabel = false)
                AestheticAppIcon("Browser", Icons.Default.Language, theme.accentColor, showLabel = false)
                AestheticAppIcon("Camera", Icons.Default.CameraAlt, theme.accentColor, showLabel = false)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Samsung Nav Bar line
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.65f))
            )
        }
    }
}

@Composable
private fun AestheticAppIcon(
    name: String,
    icon: ImageVector,
    tintColor: Color,
    showLabel: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Color.Black.copy(alpha = 0.45f))
                .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = tintColor,
                modifier = Modifier.size(22.dp)
            )
        }
        if (showLabel) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
