package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.data.FavoriteWallpaperSetEntity
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.LockScreenConfig
import com.example.model.ThemeCategory
import com.example.model.ThemeItem
import com.example.model.ThemeRepository
import com.example.ui.components.S24UltraFrame
import com.example.ui.components.ScreenPreviewMode
import com.example.ui.theme.AestheticAccent
import com.example.ui.theme.S24BorderDark
import com.example.ui.theme.S24CardDark
import com.example.ui.theme.S24SurfaceDark
import com.example.ui.theme.S24TextMuted
import com.example.ui.theme.S24TextPrimary
import com.example.ui.theme.S24TextSecondary
import com.example.ui.theme.S24TitaniumBlack

@Composable
fun ThemeGalleryScreen(
    language: AppLanguage,
    favoriteIds: Set<String>,
    quickAccessWallpapers: List<FavoriteWallpaperSetEntity> = emptyList(),
    onToggleBookmark: (ThemeItem) -> Unit,
    onSelectThemeForCustomizer: (ThemeItem) -> Unit,
    onQuickApplyTheme: (ThemeItem) -> Unit,
    onOpenTutorial: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(ThemeCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var activeGeminiTag by remember { mutableStateOf<String?>(null) }

    val popularGeminiTags = remember {
        listOf(
            "Minimal",
            "Neon",
            "Cyberpunk",
            "OLED",
            "Cozy",
            "Sunset",
            "Architecture",
            "Forest",
            "Quantum"
        )
    }

    val filteredThemes = remember(selectedCategory, favoriteIds, searchQuery, activeGeminiTag) {
        var list = when (selectedCategory) {
            ThemeCategory.ALL -> ThemeRepository.themes
            ThemeCategory.FAVORITES -> ThemeRepository.themes.filter { favoriteIds.contains(it.id) }
            else -> ThemeRepository.themes.filter { it.category == selectedCategory }
        }

        val trimmed = searchQuery.trim().lowercase()
        if (trimmed.isNotEmpty()) {
            list = list.filter { theme ->
                val matchesTag = theme.tags.any { it.lowercase().contains(trimmed) }
                val matchesTitle = theme.titleEn.lowercase().contains(trimmed) || theme.titleGe.lowercase().contains(trimmed)
                val matchesSubtitle = theme.subtitleEn.lowercase().contains(trimmed) || theme.subtitleGe.lowercase().contains(trimmed)
                val matchesCategory = theme.category.name.lowercase().contains(trimmed)
                matchesTag || matchesTitle || matchesSubtitle || matchesCategory
            }
        }

        if (activeGeminiTag != null) {
            val tagLower = activeGeminiTag!!.lowercase()
            list = list.filter { theme ->
                theme.tags.any { it.lowercase().contains(tagLower) }
            }
        }

        list
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(S24TitaniumBlack),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero Header
        item {
            GalleryHeroBanner(
                language = language,
                onOpenTutorial = onOpenTutorial
            )
        }

        // Search Bar at Top of Wallpaper Gallery
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        if (activeGeminiTag != null && it != activeGeminiTag) {
                            activeGeminiTag = null
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("wallpaper_search_bar"),
                    placeholder = {
                        Text(
                            text = if (language == AppLanguage.GEORGIAN)
                                "მოძებნეთ Gemini ტეგებით (მაგ: Minimal, Neon, OLED)..."
                            else
                                "Search by Gemini tags (e.g. Minimal, Neon, OLED)...",
                            fontSize = 12.sp,
                            color = S24TextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = AestheticAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    searchQuery = ""
                                    activeGeminiTag = null
                                },
                                modifier = Modifier.testTag("clear_search_query_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = S24TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = S24CardDark,
                        unfocusedContainerColor = S24CardDark,
                        focusedBorderColor = AestheticAccent,
                        unfocusedBorderColor = S24BorderDark,
                        focusedTextColor = S24TextPrimary,
                        unfocusedTextColor = S24TextPrimary,
                        cursorColor = AestheticAccent
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Gemini Quick Tag Chips Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AestheticAccent,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "ტეგები:" else "AI Tags:",
                            color = AestheticAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(popularGeminiTags) { tag ->
                            val isSelected = (activeGeminiTag.equals(tag, ignoreCase = true)) ||
                                    (searchQuery.equals(tag, ignoreCase = true))
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        searchQuery = ""
                                        activeGeminiTag = null
                                    } else {
                                        searchQuery = tag
                                        activeGeminiTag = tag
                                    }
                                },
                                label = {
                                    Text(
                                        text = "#$tag",
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AestheticAccent,
                                    selectedLabelColor = Color.Black,
                                    containerColor = Color.White.copy(alpha = 0.06f),
                                    labelColor = S24TextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) AestheticAccent else S24BorderDark
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("gemini_tag_chip_${tag.lowercase()}")
                            )
                        }
                    }
                }
            }
        }

        // Quick Access Carousel Tray if favorites exist
        if (quickAccessWallpapers.isNotEmpty()) {
            item {
                QuickAccessTray(
                    wallpapers = quickAccessWallpapers,
                    language = language,
                    onSelectTheme = onSelectThemeForCustomizer,
                    onQuickApply = onQuickApplyTheme
                )
            }
        }

        // Category Filter Chips
        item {
            CategoryChipsRow(
                selectedCategory = selectedCategory,
                onSelectCategory = { selectedCategory = it },
                favoriteCount = favoriteIds.size,
                language = language
            )
        }

        // Search Query Indicator / Results Counter
        if (searchQuery.isNotEmpty() || activeGeminiTag != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.GEORGIAN)
                            "ნაპოვნია ${filteredThemes.size} ნაკრები ტეგით '${searchQuery.ifEmpty { activeGeminiTag ?: "" }}'"
                        else
                            "Found ${filteredThemes.size} sets for '${searchQuery.ifEmpty { activeGeminiTag ?: "" }}'",
                        color = S24TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "გასუფთავება" else "Clear",
                        color = AestheticAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable {
                                searchQuery = ""
                                activeGeminiTag = null
                            }
                            .padding(4.dp)
                    )
                }
            }
        }

        // Empty Search Results State
        if (filteredThemes.isEmpty() && (searchQuery.isNotEmpty() || activeGeminiTag != null)) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = S24TextMuted,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Text(
                            text = if (language == AppLanguage.GEORGIAN)
                                "ტეგით '$searchQuery' ფონები ვერ მოიძებნა"
                            else
                                "No wallpapers matching '$searchQuery'",
                            color = S24TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (language == AppLanguage.GEORGIAN)
                                "სცადეთ სხვა Gemini ტეგები, როგორიცაა Minimal, Neon, OLED, ან Architecture"
                            else
                                "Try popular Gemini tags like Minimal, Neon, OLED, or Architecture",
                            color = S24TextSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = {
                                searchQuery = ""
                                activeGeminiTag = null
                            },
                            modifier = Modifier.testTag("clear_search_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AestheticAccent,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "ძებნის გასუფთავება" else "Clear Filter",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            // Themes List
            items(filteredThemes, key = { it.id }) { theme ->
                ThemeCard(
                    theme = theme,
                    language = language,
                    isBookmarked = favoriteIds.contains(theme.id),
                    onToggleBookmark = { onToggleBookmark(theme) },
                    onCustomize = { onSelectThemeForCustomizer(theme) },
                    onApply = { onQuickApplyTheme(theme) }
                )
            }
        }
    }
}

@Composable
private fun GalleryHeroBanner(
    language: AppLanguage,
    onOpenTutorial: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1E1738),
                        Color(0xFF121422),
                        Color(0xFF1F1B2C)
                    )
                )
            )
            .border(1.dp, AestheticAccent.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AestheticAccent.copy(alpha = 0.25f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Samsung S24 Ultra",
                        color = AestheticAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF5CE1E6).copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "One UI 6.1",
                        color = Color(0xFF5CE1E6),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (language == AppLanguage.GEORGIAN) "9 ესთეტიკური ნაკრები და ვიჯეტები" else "9 Aesthetic Wallpaper & Widget Sets",
                color = S24TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (language == AppLanguage.GEORGIAN)
                    "შთაგონებული iThemeDesign-ის ექსკლუზიური თემებით. შექმნილია სპეციალურად Galaxy S24 Ultra-ს QHD+ AMOLED ეკრანისთვის."
                else
                    "Inspired by iThemeDesign premium drops. Optimized exclusively for Galaxy S24 Ultra's QHD+ Dynamic AMOLED display.",
                color = S24TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onOpenTutorial,
                    modifier = Modifier.testTag("open_tutorial_hero_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AestheticAccent,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "დაყენების გზამკვლევი" else "S24 Setup Tutorial",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryChipsRow(
    selectedCategory: ThemeCategory,
    onSelectCategory: (ThemeCategory) -> Unit,
    favoriteCount: Int,
    language: AppLanguage
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val categories = listOf(
            ThemeCategory.ALL to (if (language == AppLanguage.GEORGIAN) "ყველა" else "All"),
            ThemeCategory.FAVORITES to (if (language == AppLanguage.GEORGIAN) "რჩეულები ($favoriteCount)" else "Favorites ($favoriteCount)"),
            ThemeCategory.MINIMALIST to (if (language == AppLanguage.GEORGIAN) "მინიმალისტური" else "Minimalist"),
            ThemeCategory.AMOLED_DARK to (if (language == AppLanguage.GEORGIAN) "AMOLED Dark" else "AMOLED Dark"),
            ThemeCategory.CYBERPUNK to (if (language == AppLanguage.GEORGIAN) "კიბერპანკი" else "Cyberpunk"),
            ThemeCategory.AESTHETIC_COZY to (if (language == AppLanguage.GEORGIAN) "Cozy Lo-Fi" else "Cozy Lo-Fi"),
            ThemeCategory.NATURE to (if (language == AppLanguage.GEORGIAN) "ბუნება & დიუნები" else "Nature & Dunes")
        )

        items(categories) { (cat, label) ->
            val isSelected = selectedCategory == cat
            FilterChip(
                selected = isSelected,
                onClick = { onSelectCategory(cat) },
                label = {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AestheticAccent,
                    selectedLabelColor = Color.Black,
                    containerColor = S24CardDark,
                    labelColor = S24TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = S24BorderDark,
                    selectedBorderColor = AestheticAccent
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
private fun ThemeCard(
    theme: ThemeItem,
    language: AppLanguage,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onCustomize: () -> Unit,
    onApply: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("theme_card_${theme.id}"),
        colors = CardDefaults.cardColors(containerColor = S24CardDark),
        shape = RoundedCornerShape(20.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(S24BorderDark, S24BorderDark)))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Preview & Meta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Interactive S24 Ultra mini preview mockup
                Box(
                    modifier = Modifier
                        .width(115.dp)
                        .height(240.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onCustomize)
                ) {
                    val previewConfig = remember(theme) {
                        LockScreenConfig(
                            clockStyle = theme.defaultClockStyle,
                            clockColor = theme.clockColor
                        )
                    }
                    S24UltraFrame(
                        theme = theme,
                        config = previewConfig,
                        language = language,
                        previewMode = ScreenPreviewMode.LOCK_SCREEN,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Details & Controls
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(240.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) theme.titleGe else theme.titleEn,
                                color = S24TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )

                            // Dedicated Quick Access UI Toggle Pill
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isBookmarked) AestheticAccent.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.08f))
                                    .border(1.dp, if (isBookmarked) AestheticAccent else S24BorderDark, RoundedCornerShape(12.dp))
                                    .clickable { onToggleBookmark() }
                                    .testTag("wallpaper_quick_access_toggle_${theme.id}")
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Save to Quick Access",
                                    tint = if (isBookmarked) AestheticAccent else S24TextMuted,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBookmarked) {
                                        if (language == AppLanguage.GEORGIAN) "რჩეული" else "Saved"
                                    } else {
                                        if (language == AppLanguage.GEORGIAN) "შენახვა" else "Save"
                                    },
                                    color = if (isBookmarked) AestheticAccent else S24TextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (language == AppLanguage.GEORGIAN) theme.subtitleGe else theme.subtitleEn,
                            color = S24TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Tags
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            theme.tags.take(2).forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.White.copy(alpha = 0.08f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = tag,
                                        color = S24TextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    // Action buttons
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onCustomize,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("customize_button_${theme.id}"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AestheticAccent.copy(alpha = 0.2f),
                                contentColor = AestheticAccent
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "მორგება (Studio)" else "Customize",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = onApply,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("apply_button_${theme.id}"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AestheticAccent,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Wallpaper,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "დაყენება / Save" else "Set Wallpaper",
                                fontSize = 12.sp,
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
private fun QuickAccessTray(
    wallpapers: List<FavoriteWallpaperSetEntity>,
    language: AppLanguage,
    onSelectTheme: (ThemeItem) -> Unit,
    onQuickApply: (ThemeItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = AestheticAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "სწრაფი წვდომის ფონები" else "Quick Access Wallpapers",
                    color = S24TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "${wallpapers.size} ${if (language == AppLanguage.GEORGIAN) "შენახული" else "saved"}",
                color = AestheticAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(wallpapers, key = { it.id }) { item ->
                val theme = ThemeRepository.themes.find { it.id == item.id } ?: ThemeRepository.themes.first()
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(S24CardDark)
                        .border(1.dp, AestheticAccent.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                        .clickable { onSelectTheme(theme) }
                        .padding(10.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(95.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            Image(
                                painter = painterResource(id = theme.imageRes ?: com.example.R.drawable.img_theme_titanium),
                                contentDescription = theme.titleEn,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Quick Access",
                                    tint = AestheticAccent,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (language == AppLanguage.GEORGIAN) theme.titleGe else theme.titleEn,
                            color = S24TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "სწრაფი დაყენება" else "Quick Apply",
                            color = AestheticAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clickable { onQuickApply(theme) }
                                .padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
