package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ClockStyle
import com.example.model.LockScreenConfig
import com.example.model.ThemeItem
import com.example.model.WidgetType
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeScreen(
    theme: ThemeItem,
    config: LockScreenConfig,
    language: AppLanguage,
    isFavorite: Boolean = false,
    onToggleFavorite: (() -> Unit)? = null,
    onUpdateConfig: (LockScreenConfig) -> Unit,
    onApplyTheme: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var previewMode by remember { mutableStateOf(ScreenPreviewMode.LOCK_SCREEN) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Clock, 1: Color, 2: Widgets, 3: Depth & Effects

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(S24TitaniumBlack)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("back_button_customizer")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = S24TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) theme.titleGe else theme.titleEn,
                        color = S24TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "S24 Ultra მორგების სტუდია" else "S24 Ultra Customizer Studio",
                        color = S24TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onToggleFavorite != null) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.testTag("customizer_quick_access_toggle")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Quick Access",
                            tint = if (isFavorite) AestheticAccent else S24TextSecondary
                        )
                    }
                }

                Button(
                    onClick = onApplyTheme,
                    modifier = Modifier.testTag("apply_from_customizer_button"),
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
                        text = if (language == AppLanguage.GEORGIAN) "დაყენება" else "Apply",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mode Switcher Pill (Lock Screen vs Home Screen)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(S24CardDark)
                    .border(1.dp, S24BorderDark, RoundedCornerShape(20.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val isLock = previewMode == ScreenPreviewMode.LOCK_SCREEN
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isLock) AestheticAccent else Color.Transparent)
                        .clickable { previewMode = ScreenPreviewMode.LOCK_SCREEN }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isLock) Color.Black else S24TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "დაბლოკილი ეკრანი" else "Lock Screen",
                            color = if (isLock) Color.Black else S24TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isLock) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                val isHome = previewMode == ScreenPreviewMode.HOME_SCREEN
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isHome) AestheticAccent else Color.Transparent)
                        .clickable { previewMode = ScreenPreviewMode.HOME_SCREEN }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = if (isHome) Color.Black else S24TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "მთავარი ეკრანი" else "Home Screen",
                            color = if (isHome) Color.Black else S24TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isHome) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Realistic S24 Ultra Frame
            Box(
                modifier = Modifier
                    .width(175.dp)
                    .height(370.dp)
            ) {
                S24UltraFrame(
                    theme = theme,
                    config = config,
                    language = language,
                    previewMode = previewMode,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Customization Tabs
            val tabs = listOf(
                (if (language == AppLanguage.GEORGIAN) "საათი" else "Clock"),
                (if (language == AppLanguage.GEORGIAN) "ფერი" else "Color"),
                (if (language == AppLanguage.GEORGIAN) "ვიჯეტები" else "Widgets"),
                (if (language == AppLanguage.GEORGIAN) "ეფექტები" else "Effects")
            )

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = S24CardDark,
                contentColor = AestheticAccent,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = AestheticAccent
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) AestheticAccent else S24TextSecondary
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Panels
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(S24CardDark)
                    .border(1.dp, S24BorderDark, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                when (selectedTab) {
                    0 -> ClockStylePanel(
                        selectedStyle = config.clockStyle,
                        onSelectStyle = { onUpdateConfig(config.copy(clockStyle = it)) },
                        language = language
                    )
                    1 -> ClockColorPanel(
                        selectedColor = config.clockColor,
                        onSelectColor = { onUpdateConfig(config.copy(clockColor = it)) },
                        language = language
                    )
                    2 -> WidgetsPanel(
                        activeWidgets = config.activeWidgets,
                        onToggleWidget = { widget ->
                            val updated = if (config.activeWidgets.contains(widget)) {
                                config.activeWidgets - widget
                            } else {
                                config.activeWidgets + widget
                            }
                            onUpdateConfig(config.copy(activeWidgets = updated))
                        },
                        showNotification = config.showNotificationPill,
                        onToggleNotification = { onUpdateConfig(config.copy(showNotificationPill = it)) },
                        language = language
                    )
                    3 -> EffectsPanel(
                        config = config,
                        onUpdateConfig = onUpdateConfig,
                        language = language
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun ClockStylePanel(
    selectedStyle: ClockStyle,
    onSelectStyle: (ClockStyle) -> Unit,
    language: AppLanguage
) {
    val styles = listOf(
        ClockStyle.SERIF_BOLD to (if (language == AppLanguage.GEORGIAN) "Bold Serif (Luxury)" else "Bold Serif (Luxury)"),
        ClockStyle.CLEAN_SANS to (if (language == AppLanguage.GEORGIAN) "Modern Sans (One UI)" else "Modern Sans (One UI)"),
        ClockStyle.STACKED_NUMBERS to (if (language == AppLanguage.GEORGIAN) "Stacked Digits" else "Stacked Digits"),
        ClockStyle.THIN_ROMAN to (if (language == AppLanguage.GEORGIAN) "Thin Roman" else "Thin Roman"),
        ClockStyle.HANDWRITTEN to (if (language == AppLanguage.GEORGIAN) "Handwritten Lo-Fi" else "Handwritten Lo-Fi"),
        ClockStyle.PILL_DIGITAL to (if (language == AppLanguage.GEORGIAN) "Pill Capsule" else "Pill Capsule")
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        styles.forEach { (style, name) ->
            val isSelected = selectedStyle == style
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) AestheticAccent.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.2f))
                    .border(1.dp, if (isSelected) AestheticAccent else S24BorderDark, RoundedCornerShape(12.dp))
                    .clickable { onSelectStyle(style) }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    color = if (isSelected) S24TextPrimary else S24TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = AestheticAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ClockColorPanel(
    selectedColor: Color,
    onSelectColor: (Color) -> Unit,
    language: AppLanguage
) {
    val colors = listOf(
        Color(0xFFFFFFFF) to "Pure White",
        Color(0xFFFBF4EB) to "Titanium Cream",
        Color(0xFFE9D5FF) to "Lilac Dust",
        Color(0xFF67E8F9) to "Cyber Cyan",
        Color(0xFFFDE68A) to "Warm Gold",
        Color(0xFFFECDD3) to "Blush Rose",
        Color(0xFFA7F3D0) to "Mint Leaf",
        Color(0xFFFED7AA) to "Peach Sunset"
    )

    Column {
        Text(
            text = if (language == AppLanguage.GEORGIAN) "აირჩიეთ საათისა და ვიჯეტების ფერი:" else "Select clock & widget text color:",
            color = S24TextSecondary,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            colors.forEach { (color, label) ->
                val isSelected = selectedColor == color
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onSelectColor(color) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) AestheticAccent else Color.White.copy(alpha = 0.2f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = label,
                        color = if (isSelected) S24TextPrimary else S24TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun WidgetsPanel(
    activeWidgets: List<WidgetType>,
    onToggleWidget: (WidgetType) -> Unit,
    showNotification: Boolean,
    onToggleNotification: (Boolean) -> Unit,
    language: AppLanguage
) {
    val widgets = listOf(
        WidgetType.BATTERY_RING to (if (language == AppLanguage.GEORGIAN) "ელემენტის რგოლი (98%)" else "Battery Ring (98%)"),
        WidgetType.WEATHER to (if (language == AppLanguage.GEORGIAN) "ამინდი (22° მზიანი)" else "Weather Widget (22° Sunny)"),
        WidgetType.STEP_COUNTER to (if (language == AppLanguage.GEORGIAN) "ნაბიჯების მრიცხველი (7.8k)" else "Step Counter (7.8k)"),
        WidgetType.CALENDAR_EVENT to (if (language == AppLanguage.GEORGIAN) "კალენდრის ივენთი (14:00)" else "Calendar Event (14:00)"),
        WidgetType.MUSIC_PLAYER to (if (language == AppLanguage.GEORGIAN) "მუსიკის პლეერი (M83)" else "Music Player Mini Widget"),
        WidgetType.QUOTE_TEXT to (if (language == AppLanguage.GEORGIAN) "ესთეტიკური ტექსტი / ციტატა" else "Aesthetic Quote Pill")
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        widgets.forEach { (type, label) ->
            val isActive = activeWidgets.contains(type)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.2f))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    color = if (isActive) S24TextPrimary else S24TextSecondary,
                    fontSize = 13.sp
                )
                Switch(
                    checked = isActive,
                    onCheckedChange = { onToggleWidget(type) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = AestheticAccent
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.2f))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (language == AppLanguage.GEORGIAN) "შეტყობინების კაფსულა (Pill)" else "Show Notification Capsule",
                color = S24TextPrimary,
                fontSize = 13.sp
            )
            Switch(
                checked = showNotification,
                onCheckedChange = onToggleNotification,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    checkedTrackColor = AestheticAccent
                )
            )
        }
    }
}

@Composable
private fun EffectsPanel(
    config: LockScreenConfig,
    onUpdateConfig: (LockScreenConfig) -> Unit,
    language: AppLanguage
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Home Screen Blur slider
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "მთავარი ეკრანის დაბინდვა (Blur)" else "Home Screen Blur",
                    color = S24TextPrimary,
                    fontSize = 13.sp
                )
                Text(
                    text = "${config.homeScreenBlur.toInt()} dp",
                    color = AestheticAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Slider(
                value = config.homeScreenBlur,
                onValueChange = { onUpdateConfig(config.copy(homeScreenBlur = it)) },
                valueRange = 0f..25f,
                colors = SliderDefaults.colors(
                    thumbColor = AestheticAccent,
                    activeTrackColor = AestheticAccent
                )
            )
        }

        // Brightness adjustment
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "სიკაშკაშის კორექცია" else "Brightness",
                    color = S24TextPrimary,
                    fontSize = 13.sp
                )
                Text(
                    text = "${(config.brightnessAdjustment * 100).toInt()}%",
                    color = AestheticAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Slider(
                value = config.brightnessAdjustment,
                onValueChange = { onUpdateConfig(config.copy(brightnessAdjustment = it)) },
                valueRange = -0.3f..0.3f,
                colors = SliderDefaults.colors(
                    thumbColor = AestheticAccent,
                    activeTrackColor = AestheticAccent
                )
            )
        }

        // Vignette toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.2f))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (language == AppLanguage.GEORGIAN) "კუთხეების დაბნელება (Vignette)" else "Vignette Dark Corners",
                color = S24TextPrimary,
                fontSize = 13.sp
            )
            Switch(
                checked = config.isVignetteActive,
                onCheckedChange = { onUpdateConfig(config.copy(isVignetteActive = it)) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    checkedTrackColor = AestheticAccent
                )
            )
        }
    }
}
