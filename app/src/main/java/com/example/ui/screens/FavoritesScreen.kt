package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FavoriteThemeEntity
import com.example.model.AppLanguage
import com.example.model.ClockStyle
import com.example.model.LockScreenConfig
import com.example.model.ThemeItem
import com.example.model.ThemeRepository
import com.example.model.WidgetType
import com.example.ui.components.S24UltraFrame
import com.example.ui.components.ScreenPreviewMode
import com.example.ui.theme.AestheticAccent
import com.example.ui.theme.S24BorderDark
import com.example.ui.theme.S24CardDark
import com.example.ui.theme.S24TextMuted
import com.example.ui.theme.S24TextPrimary
import com.example.ui.theme.S24TextSecondary
import com.example.ui.theme.S24TitaniumBlack
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FavoritesScreen(
    favorites: List<FavoriteThemeEntity>,
    categories: List<String>,
    selectedCategory: String?,
    onSelectCategory: (String?) -> Unit,
    isGeneratingAi: Boolean,
    aiStatusMessage: String?,
    language: AppLanguage,
    onSelectForCustomizer: (ThemeItem, LockScreenConfig) -> Unit,
    onQuickApply: (ThemeItem, LockScreenConfig) -> Unit,
    onGenerateAiTags: (ThemeItem, LockScreenConfig) -> Unit,
    onRemoveFavorite: (String) -> Unit,
    onExploreThemes: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (favorites.isEmpty() && selectedCategory == null) {
        EmptyFavoritesState(language = language, onExploreThemes = onExploreThemes, modifier = modifier)
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(S24TitaniumBlack),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF43F5E).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFF43F5E),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "შენახული ფონები & ვიჯეტები" else "Saved Wallpapers & Widgets",
                            color = S24TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (language == AppLanguage.GEORGIAN)
                                "${favorites.size} შენახული ნაკრები (Room Database SQLite)"
                            else
                                "${favorites.size} saved sets (Local Room Database)",
                            color = S24TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // AI Status Banner if generating
            if (isGeneratingAi || !aiStatusMessage.isNullOrBlank()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(AestheticAccent.copy(alpha = 0.15f))
                            .border(1.dp, AestheticAccent.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isGeneratingAi) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = AestheticAccent,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = AestheticAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = aiStatusMessage ?: "Gemini API Active",
                            color = S24TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Category Filter Chips from Room DB
            if (categories.isNotEmpty()) {
                item {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Category,
                                contentDescription = null,
                                tint = AestheticAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "კატეგორიით ფილტრაცია (Room)" else "Filter by Category (Room)",
                                color = S24TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                val isAllSelected = selectedCategory == null
                                FilterChip(
                                    selected = isAllSelected,
                                    onClick = { onSelectCategory(null) },
                                    label = { Text("All (${favorites.size})", fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = AestheticAccent,
                                        selectedLabelColor = Color.Black,
                                        containerColor = S24CardDark,
                                        labelColor = S24TextSecondary
                                    )
                                )
                            }

                            items(categories) { cat ->
                                val isSelected = selectedCategory.equals(cat, ignoreCase = true)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onSelectCategory(if (isSelected) null else cat) },
                                    label = { Text(cat, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = AestheticAccent,
                                        selectedLabelColor = Color.Black,
                                        containerColor = S24CardDark,
                                        labelColor = S24TextSecondary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            items(favorites, key = { it.themeId }) { entity ->
                val theme = ThemeRepository.themes.firstOrNull { it.id == entity.themeId }
                    ?: ThemeRepository.themes.first()

                val clockStyle = try {
                    ClockStyle.valueOf(entity.clockStyleName)
                } catch (e: Exception) {
                    theme.defaultClockStyle
                }

                val activeWidgets = entity.activeWidgetsCsv.split(",")
                    .mapNotNull { name ->
                        try { WidgetType.valueOf(name) } catch (e: Exception) { null }
                    }

                val config = LockScreenConfig(
                    clockStyle = clockStyle,
                    clockColor = Color(entity.clockColorLong.toInt()),
                    activeWidgets = activeWidgets,
                    homeScreenBlur = entity.homeScreenBlur,
                    isVignetteActive = entity.isVignetteActive
                )

                FavoriteItemCard(
                    entity = entity,
                    theme = theme,
                    config = config,
                    isGeneratingAi = isGeneratingAi,
                    language = language,
                    onCustomize = { onSelectForCustomizer(theme, config) },
                    onApply = { onQuickApply(theme, config) },
                    onGenerateAiTags = { onGenerateAiTags(theme, config) },
                    onDelete = { onRemoveFavorite(entity.themeId) }
                )
            }
        }
    }
}

@Composable
private fun FavoriteItemCard(
    entity: FavoriteThemeEntity,
    theme: ThemeItem,
    config: LockScreenConfig,
    isGeneratingAi: Boolean,
    language: AppLanguage,
    onCustomize: () -> Unit,
    onApply: () -> Unit,
    onGenerateAiTags: () -> Unit,
    onDelete: () -> Unit
) {
    val dateStr = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(entity.timestamp))
    val tagsList = entity.generatedTagsCsv.split(",").filter { it.isNotBlank() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("saved_favorite_card_${theme.id}"),
        colors = CardDefaults.cardColors(containerColor = S24CardDark),
        shape = RoundedCornerShape(20.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(S24BorderDark, S24BorderDark)))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // S24 Ultra mini preview
                Box(
                    modifier = Modifier
                        .width(115.dp)
                        .height(260.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onCustomize)
                ) {
                    S24UltraFrame(
                        theme = theme,
                        config = config,
                        language = language,
                        previewMode = ScreenPreviewMode.LOCK_SCREEN,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Metadata & Actions
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(260.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.GEORGIAN) theme.titleGe else theme.titleEn,
                                    color = S24TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                // Category Pill
                                Box(
                                    modifier = Modifier
                                        .padding(top = 3.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(AestheticAccent.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = entity.primaryCategory,
                                        color = AestheticAccent,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            IconButton(
                                onClick = onDelete,
                                modifier = Modifier.size(26.dp).testTag("delete_favorite_${theme.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFF43F5E),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // AI Tags
                        if (tagsList.isNotEmpty()) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                tagsList.take(2).forEach { tag ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color.White.copy(alpha = 0.08f))
                                            .padding(horizontal = 5.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = tag, color = S24TextSecondary, fontSize = 9.sp)
                                    }
                                }
                            }
                        }

                        if (entity.vibeDescription.isNotBlank()) {
                            Text(
                                text = entity.vibeDescription,
                                color = S24TextMuted,
                                fontSize = 10.sp,
                                lineHeight = 13.sp,
                                maxLines = 2
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "შენახულია: $dateStr" else "Saved: $dateStr",
                            color = S24TextMuted,
                            fontSize = 10.sp
                        )
                    }

                    // Action buttons
                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        // AI Generate Tags Button
                        Button(
                            onClick = onGenerateAiTags,
                            enabled = !isGeneratingAi,
                            modifier = Modifier.fillMaxWidth().testTag("ai_tag_button_${theme.id}"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF5CE1E6).copy(alpha = 0.2f),
                                contentColor = Color(0xFF5CE1E6)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "✨ Gemini AI ტეგები" else "✨ Gemini AI Tags",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = onCustomize,
                            modifier = Modifier.fillMaxWidth().testTag("customize_saved_${theme.id}"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AestheticAccent.copy(alpha = 0.2f),
                                contentColor = AestheticAccent
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "მორგება" else "Edit in Studio",
                                fontSize = 10.sp
                            )
                        }

                        Button(
                            onClick = onApply,
                            modifier = Modifier.fillMaxWidth().testTag("apply_saved_${theme.id}"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AestheticAccent,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Wallpaper, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "დაყენება" else "Apply to S24",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyFavoritesState(
    language: AppLanguage,
    onExploreThemes: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(S24TitaniumBlack)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = S24TextMuted,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = if (language == AppLanguage.GEORGIAN) "შენახული ფონები არ არის" else "No Saved Favorites Yet",
                color = S24TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (language == AppLanguage.GEORGIAN)
                    "დააჭირეთ სანიშნის (Bookmark) ღილაკს ნებისმიერ ფონზე, რათა შეინახოთ ადგილობრივ Room ბაზაში და გენერირდეს Gemini AI ტეგები."
                else
                    "Tap the bookmark icon on any wallpaper to save it to your local Room database and generate Gemini AI tags.",
                color = S24TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 24.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = onExploreThemes,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AestheticAccent,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "ფონების დათვალიერება" else "Explore Wallpapers",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
