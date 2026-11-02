package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ThemeItem
import com.example.model.ThemeRepository
import com.example.ui.components.AestheticBatteryWidget
import com.example.ui.components.AestheticClockWidget
import com.example.ui.components.AestheticWeatherWidget
import com.example.ui.theme.AestheticAccent
import com.example.ui.theme.S24BorderDark
import com.example.ui.theme.S24CardDark
import com.example.ui.theme.S24SurfaceDark
import com.example.ui.theme.S24TextMuted
import com.example.ui.theme.S24TextPrimary
import com.example.ui.theme.S24TextSecondary
import com.example.ui.theme.S24TitaniumBlack
import com.example.widget.BatteryTelemetryUtil
import com.example.widget.DeviceBatteryTelemetry
import com.example.widget.WidgetPinHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

enum class WidgetCategoryFilter {
    ALL,
    CLOCK,
    BATTERY,
    GLANCE
}

enum class CustomWidgetType {
    ONE_UI_DIGITAL_CLOCK,
    EDITORIAL_SERIF_CLOCK,
    ANALOG_MODERN_CLOCK,
    DUAL_WORLD_CLOCK,
    ECOSYSTEM_BATTERY_RINGS,
    LINEAR_POWER_DECK,
    COMPACT_BATTERY_PILL,
    WEATHER_GLANCE
}

data class WidgetPreset(
    val type: CustomWidgetType,
    val titleEn: String,
    val titleGe: String,
    val subtitleEn: String,
    val subtitleGe: String,
    val category: WidgetCategoryFilter,
    val sizeLabel: String
)

@Composable
fun WidgetsScreen(
    language: AppLanguage,
    onNavigateToThemes: () -> Unit,
    onNavigateToTutorial: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var activeCategory by remember { mutableStateOf(WidgetCategoryFilter.ALL) }
    var selectedWidgetType by remember { mutableStateOf(CustomWidgetType.ONE_UI_DIGITAL_CLOCK) }

    // Live Device Battery Telemetry
    var batteryInfo by remember { mutableStateOf(BatteryTelemetryUtil.getBatteryInfo(context)) }

    // Customizer Parameters
    var widgetOpacity by remember { mutableFloatStateOf(0.45f) }
    var widgetCornerRadius by remember { mutableFloatStateOf(24f) }
    var selectedAccentColor by remember { mutableStateOf(Color(0xFF6C5CE7)) } // Titanium Violet
    var is24hFormat by remember { mutableStateOf(true) }
    var showSeconds by remember { mutableStateOf(true) }
    var simulatedCharging by remember { mutableStateOf(false) }

    // Status Message Banner
    var bannerMessage by remember { mutableStateOf<String?>(null) }

    // Periodic battery update
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            batteryInfo = BatteryTelemetryUtil.getBatteryInfo(context)
        }
    }

    val widgetPresets = remember {
        listOf(
            WidgetPreset(
                type = CustomWidgetType.ONE_UI_DIGITAL_CLOCK,
                titleEn = "One UI 6.1 Digital Clock",
                titleGe = "One UI 6.1 ციფრული საათი",
                subtitleEn = "Clean titanium typography with live date & aesthetic glass blur",
                subtitleGe = "სუფთა ტიტანის შრიფტი თარიღით და მინის ეფექტით",
                category = WidgetCategoryFilter.CLOCK,
                sizeLabel = "4 × 2"
            ),
            WidgetPreset(
                type = CustomWidgetType.ECOSYSTEM_BATTERY_RINGS,
                titleEn = "Galaxy Ecosystem Battery Rings",
                titleGe = "Galaxy ეკოსისტემის ელემენტის რგოლები",
                subtitleEn = "S24 Ultra, Galaxy Watch7, and Buds3 Pro status with live telemetry",
                subtitleGe = "S24 Ultra, Galaxy Watch7 და Buds3 Pro ცოცხალი მუხტით",
                category = WidgetCategoryFilter.BATTERY,
                sizeLabel = "4 × 2"
            ),
            WidgetPreset(
                type = CustomWidgetType.ANALOG_MODERN_CLOCK,
                titleEn = "Aesthetic Analog Dial",
                titleGe = "ესთეტიკური ანალოგური ციფერბლატი",
                subtitleEn = "Minimalist dial with smooth ticking second hand & titanium center",
                subtitleGe = "მინიმალისტური ციფერბლატი წამმზომით და ტიტანის ღერძით",
                category = WidgetCategoryFilter.CLOCK,
                sizeLabel = "2 × 2"
            ),
            WidgetPreset(
                type = CustomWidgetType.LINEAR_POWER_DECK,
                titleEn = "Linear Power Deck & S-Pen",
                titleGe = "ხაზოვანი სიმძლავრის პანელი & S-Pen",
                subtitleEn = "Performance profile, S-Pen telemetry & estimated battery runtime",
                subtitleGe = "სიმძლავრის პროფილი, S-Pen მუხტი და დარჩენილი დრო",
                category = WidgetCategoryFilter.BATTERY,
                sizeLabel = "4 × 1"
            ),
            WidgetPreset(
                type = CustomWidgetType.DUAL_WORLD_CLOCK,
                titleEn = "Dual World Clock (Tbilisi • Seoul)",
                titleGe = "ორმაგი მსოფლიო საათი (თბილისი • სეული)",
                subtitleEn = "Dual timezone layout inspired by One UI Travel Companion",
                subtitleGe = "ორმაგი დროის სარტყელი მოგზაურთათვის",
                category = WidgetCategoryFilter.CLOCK,
                sizeLabel = "4 × 2"
            ),
            WidgetPreset(
                type = CustomWidgetType.COMPACT_BATTERY_PILL,
                titleEn = "Compact Voltage & Temp Pill",
                titleGe = "კომპაქტური ვოლტაჟის & ტემპერატურის აბი",
                subtitleEn = "Minimalist telemetry showing battery health, mV and temperature",
                subtitleGe = "მინიმალისტური ტელემეტრია: ვოლტაჟი და ტემპერატურა",
                category = WidgetCategoryFilter.BATTERY,
                sizeLabel = "2 × 1"
            ),
            WidgetPreset(
                type = CustomWidgetType.WEATHER_GLANCE,
                titleEn = "One UI Smart Weather & Glance",
                titleGe = "One UI ჭკვიანი ამინდი & შეჯამება",
                subtitleEn = "Atmospheric conditions, UV, wind, and interactive climate cycle",
                subtitleGe = "ატმოსფერული მონაცემები, UV, ქარი და ინტერაქტიული ამინდი",
                category = WidgetCategoryFilter.GLANCE,
                sizeLabel = "4 × 2"
            )
        )
    }

    val filteredPresets = widgetPresets.filter {
        when (activeCategory) {
            WidgetCategoryFilter.ALL -> true
            WidgetCategoryFilter.CLOCK -> it.category == WidgetCategoryFilter.CLOCK
            WidgetCategoryFilter.BATTERY -> it.category == WidgetCategoryFilter.BATTERY
            WidgetCategoryFilter.GLANCE -> it.category == WidgetCategoryFilter.GLANCE
        }
    }

    val titaniumColors = listOf(
        Color(0xFF6C5CE7) to "Titanium Violet",
        Color(0xFFFED766) to "Amber Yellow",
        Color(0xFF48CFAD) to "Emerald Mint",
        Color(0xFF5CE1E6) to "Cyber Cyan",
        Color(0xFFE9D5FF) to "Lilac Cloud",
        Color(0xFFD6CFC7) to "Natural Titanium",
        Color(0xFFFF6B6B) to "Sunset Coral"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(S24TitaniumBlack)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        // Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            selectedAccentColor.copy(alpha = 0.22f),
                            S24TitaniumBlack
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Widgets,
                                contentDescription = null,
                                tint = selectedAccentColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "S24 Ultra ვიჯეტების სტუდია" else "S24 Ultra Widgets Studio",
                                color = S24TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = if (language == AppLanguage.GEORGIAN)
                                "One UI 6.1 ესთეტიკური საათები & მრავალმოწყობილობიანი ელემენტი"
                            else
                                "One UI 6.1 Aesthetic Clocks & Multi-Device Battery Hub",
                            color = S24TextSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Direct Tutorial button
                    IconButton(
                        onClick = onNavigateToTutorial,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(S24CardDark)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Setup Guide",
                            tint = AestheticAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Real device telemetry summary badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(S24CardDark)
                        .border(1.dp, S24BorderDark, RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF48CFAD).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.BatteryChargingFull,
                                contentDescription = null,
                                tint = Color(0xFF48CFAD),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "ცოცხალი ტელემეტრია" else "Real S24 Battery Telemetry",
                                color = S24TextSecondary,
                                fontSize = 10.sp
                            )
                            Text(
                                text = "${batteryInfo.percentage}% • ${if (batteryInfo.isCharging || simulatedCharging) "Fast Charging" else "Discharging"} • ${batteryInfo.temperatureC}°C",
                                color = S24TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Test toggle button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(selectedAccentColor.copy(alpha = 0.2f))
                            .clickable { simulatedCharging = !simulatedCharging }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (simulatedCharging) "⚡ Charging" else "Plug Test",
                            color = selectedAccentColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Notification Banner
        AnimatedVisibility(visible = bannerMessage != null) {
            bannerMessage?.let { msg ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(selectedAccentColor.copy(alpha = 0.25f))
                        .border(1.dp, selectedAccentColor, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = msg,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Success",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WidgetCategoryFilter.values().forEach { cat ->
                val isSelected = activeCategory == cat
                val label = when (cat) {
                    WidgetCategoryFilter.ALL -> if (language == AppLanguage.GEORGIAN) "ყველა (7)" else "All Widgets (7)"
                    WidgetCategoryFilter.CLOCK -> if (language == AppLanguage.GEORGIAN) "საათები" else "Clocks"
                    WidgetCategoryFilter.BATTERY -> if (language == AppLanguage.GEORGIAN) "ელემენტი" else "Battery Hub"
                    WidgetCategoryFilter.GLANCE -> if (language == AppLanguage.GEORGIAN) "ჭკვიანი" else "Glance"
                }

                FilterChip(
                    selected = isSelected,
                    onClick = { activeCategory = cat },
                    label = { Text(label, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = selectedAccentColor,
                        selectedLabelColor = Color.Black,
                        containerColor = S24CardDark,
                        labelColor = S24TextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) selectedAccentColor else S24BorderDark
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("filter_chip_${cat.name.lowercase()}")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Selected Widget Interactive Live Preview Canvas
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "ინტერაქტიული ვიჯეტის გადახედვა" else "Interactive Live Preview",
                    color = S24TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                // Pin Directly Button
                Button(
                    onClick = {
                        val pinned = when (selectedWidgetType) {
                            CustomWidgetType.ONE_UI_DIGITAL_CLOCK,
                            CustomWidgetType.EDITORIAL_SERIF_CLOCK,
                            CustomWidgetType.ANALOG_MODERN_CLOCK,
                            CustomWidgetType.DUAL_WORLD_CLOCK -> {
                                WidgetPinHelper.pinClockWidget(context)
                            }
                            CustomWidgetType.ECOSYSTEM_BATTERY_RINGS,
                            CustomWidgetType.LINEAR_POWER_DECK,
                            CustomWidgetType.COMPACT_BATTERY_PILL -> {
                                WidgetPinHelper.pinBatteryWidget(context)
                            }
                            CustomWidgetType.WEATHER_GLANCE -> {
                                WidgetPinHelper.pinGlanceWidget(context)
                            }
                        }

                        bannerMessage = if (pinned) {
                            if (language == AppLanguage.GEORGIAN)
                                "ვიჯეტის დამატების ფანჯარა გაიხსნა! მოათავსეთ მთავარ ეკრანზე."
                            else
                                "Pin dialog opened! Place widget on your Samsung launcher."
                        } else {
                            if (language == AppLanguage.GEORGIAN)
                                "მთავარ ეკრანზე ხანგრძლივად დააჭირეთ და აირჩიეთ 'Aesthetic S24' ვიჯეტების სიიდან."
                            else
                                "Long-press your home screen and select 'Aesthetic S24' from widgets."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = selectedAccentColor),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("pin_to_home_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.TouchApp,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "ეკრანზე დამატება" else "Pin to Home",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // The Interactive Live Widget Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(widgetCornerRadius.dp))
                    .background(Color.Black.copy(alpha = widgetOpacity))
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(
                                selectedAccentColor.copy(alpha = 0.6f),
                                Color.White.copy(alpha = 0.2f),
                                selectedAccentColor.copy(alpha = 0.1f)
                            )
                        ),
                        shape = RoundedCornerShape(widgetCornerRadius.dp)
                    )
                    .testTag("preview_active_widget")
                    .padding(20.dp)
            ) {
                when (selectedWidgetType) {
                    CustomWidgetType.ONE_UI_DIGITAL_CLOCK -> {
                        OneUiDigitalClockComposable(
                            accentColor = selectedAccentColor,
                            is24h = is24hFormat,
                            showSeconds = showSeconds,
                            language = language
                        )
                    }
                    CustomWidgetType.EDITORIAL_SERIF_CLOCK -> {
                        AestheticClockWidget(
                            accentColor = selectedAccentColor,
                            textColor = Color.White,
                            language = language
                        )
                    }
                    CustomWidgetType.ANALOG_MODERN_CLOCK -> {
                        AnalogModernDialComposable(
                            accentColor = selectedAccentColor,
                            language = language
                        )
                    }
                    CustomWidgetType.DUAL_WORLD_CLOCK -> {
                        DualWorldClockComposable(
                            accentColor = selectedAccentColor,
                            is24h = is24hFormat,
                            language = language
                        )
                    }
                    CustomWidgetType.ECOSYSTEM_BATTERY_RINGS -> {
                        AestheticBatteryWidget(
                            accentColor = selectedAccentColor,
                            textColor = Color.White,
                            language = language
                        )
                    }
                    CustomWidgetType.LINEAR_POWER_DECK -> {
                        LinearPowerDeckComposable(
                            accentColor = selectedAccentColor,
                            batteryInfo = batteryInfo,
                            isCharging = batteryInfo.isCharging || simulatedCharging,
                            language = language
                        )
                    }
                    CustomWidgetType.COMPACT_BATTERY_PILL -> {
                        CompactBatteryPillComposable(
                            accentColor = selectedAccentColor,
                            batteryInfo = batteryInfo,
                            isCharging = batteryInfo.isCharging || simulatedCharging,
                            language = language
                        )
                    }
                    CustomWidgetType.WEATHER_GLANCE -> {
                        AestheticWeatherWidget(
                            accentColor = selectedAccentColor,
                            textColor = Color.White,
                            language = language
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Customizer Controls Accordion / Panel
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colors = CardDefaults.cardColors(containerColor = S24CardDark),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, S24BorderDark)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = selectedAccentColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "ვიჯეტის მორგება (Customizer)" else "Live Widget Customizer",
                            color = S24TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "S24 Titanium Engine",
                        color = S24TextSecondary,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Accent Palette Selector
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "ტიტანის აქცენტის ფერი:" else "Titanium Accent Tint:",
                    color = S24TextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    titaniumColors.forEach { (color, name) ->
                        val isSelected = selectedAccentColor == color
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { selectedAccentColor = color }
                                .padding(vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = name.split(" ").first(),
                                color = if (isSelected) S24TextPrimary else S24TextMuted,
                                fontSize = 9.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Opacity / Glassmorphism Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "მინის გამჭვირვალობა (Glass Blur):" else "Glass Blur Opacity:",
                        color = S24TextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "${(widgetOpacity * 100).toInt()}%",
                        color = selectedAccentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = widgetOpacity,
                    onValueChange = { widgetOpacity = it },
                    valueRange = 0.1f..0.95f,
                    colors = SliderDefaults.colors(
                        thumbColor = selectedAccentColor,
                        activeTrackColor = selectedAccentColor,
                        inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.testTag("slider_widget_opacity")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Corner Radius Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "კუთხეების სიმრგვალე (One UI Radius):" else "One UI Corner Radius:",
                        color = S24TextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "${widgetCornerRadius.toInt()} dp",
                        color = selectedAccentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = widgetCornerRadius,
                    onValueChange = { widgetCornerRadius = it },
                    valueRange = 12f..36f,
                    colors = SliderDefaults.colors(
                        thumbColor = selectedAccentColor,
                        activeTrackColor = selectedAccentColor,
                        inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.testTag("slider_corner_radius")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Format Toggles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "24-საათიანი ფორმატი" else "24-Hour Clock Format",
                        color = S24TextSecondary,
                        fontSize = 12.sp
                    )
                    Switch(
                        checked = is24hFormat,
                        onCheckedChange = { is24hFormat = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = selectedAccentColor
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Widgets Collection Grid / List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = if (language == AppLanguage.GEORGIAN) "ვიჯეტების კოლექცია (შეეხეთ ასარჩევად)" else "Available Widgets (Tap to Select)",
                color = S24TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (language == AppLanguage.GEORGIAN)
                    "აირჩიეთ ნებისმიერი დიზაინი ზემოთ გადასახედად და ეკრანზე დასამატებლად"
                else
                    "Select any widget preset above to customize and pin to launcher",
                color = S24TextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            filteredPresets.forEach { preset ->
                val isSelected = selectedWidgetType == preset.type
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) selectedAccentColor else S24BorderDark,
                            shape = RoundedCornerShape(18.dp)
                        )
                        .clickable { selectedWidgetType = preset.type }
                        .testTag("preset_card_${preset.type.name.lowercase()}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) selectedAccentColor.copy(alpha = 0.12f) else S24CardDark
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) selectedAccentColor else Color.White.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (preset.category) {
                                        WidgetCategoryFilter.CLOCK -> Icons.Default.AccessTime
                                        WidgetCategoryFilter.BATTERY -> Icons.Default.BatteryChargingFull
                                        else -> Icons.Default.Widgets
                                    },
                                    contentDescription = null,
                                    tint = if (isSelected) Color.Black else Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (language == AppLanguage.GEORGIAN) preset.titleGe else preset.titleEn,
                                        color = S24TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.White.copy(alpha = 0.1f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = preset.sizeLabel,
                                            color = selectedAccentColor,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Text(
                                    text = if (language == AppLanguage.GEORGIAN) preset.subtitleGe else preset.subtitleEn,
                                    color = S24TextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(selectedAccentColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// SUB-COMPOSABLES FOR DETAILED WIDGETS
// =========================================================================

@Composable
fun OneUiDigitalClockComposable(
    accentColor: Color,
    is24h: Boolean,
    showSeconds: Boolean,
    language: AppLanguage
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.GEORGIAN) "შაბათი, 31 ოქტომბერი • თბილისი" else "Saturday, Oct 31 • Tbilisi",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "S24 ULTRA",
                    color = accentColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (is24h) "10:30" else "10:30",
                color = Color.White,
                fontSize = 54.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = (-2).sp,
                lineHeight = 56.sp
            )
            if (showSeconds) {
                Text(
                    text = ":45",
                    color = accentColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
            if (!is24h) {
                Text(
                    text = "AM",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.GEORGIAN) "22°C • მზიანი ამინდი" else "22°C • Sunny & Clear",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp
            )
            Text(
                text = "Next: Alarm 07:00 AM",
                color = accentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AnalogModernDialComposable(
    accentColor: Color,
    language: AppLanguage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // Hand-drawn Canvas Dial
        Box(
            modifier = Modifier.size(130.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2f - 6.dp.toPx()

                // Dial Background Rim
                drawCircle(
                    color = Color.White.copy(alpha = 0.08f),
                    radius = radius,
                    center = center
                )
                drawCircle(
                    color = accentColor.copy(alpha = 0.35f),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )

                // 12 Hour Ticks
                for (i in 0 until 12) {
                    val angle = Math.toRadians((i * 30.0) - 90.0)
                    val tickLength = if (i % 3 == 0) 10.dp.toPx() else 5.dp.toPx()
                    val tickWidth = if (i % 3 == 0) 2.5.dp.toPx() else 1.2.dp.toPx()
                    val tickColor = if (i % 3 == 0) accentColor else Color.White.copy(alpha = 0.4f)

                    val startX = center.x + (radius - tickLength) * cos(angle).toFloat()
                    val startY = center.y + (radius - tickLength) * sin(angle).toFloat()
                    val endX = center.x + radius * cos(angle).toFloat()
                    val endY = center.y + radius * sin(angle).toFloat()

                    drawLine(
                        color = tickColor,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = tickWidth,
                        cap = StrokeCap.Round
                    )
                }

                // Hour Hand (pointing towards 10)
                val hourAngle = Math.toRadians((10.5 * 30.0) - 90.0)
                val hourLength = radius * 0.5f
                drawLine(
                    color = Color.White,
                    start = center,
                    end = Offset(
                        center.x + hourLength * cos(hourAngle).toFloat(),
                        center.y + hourLength * sin(hourAngle).toFloat()
                    ),
                    strokeWidth = 3.5.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Minute Hand (pointing towards 30 / 6 o'clock)
                val minAngle = Math.toRadians((30.0 * 6.0) - 90.0)
                val minLength = radius * 0.75f
                drawLine(
                    color = Color.White.copy(alpha = 0.9f),
                    start = center,
                    end = Offset(
                        center.x + minLength * cos(minAngle).toFloat(),
                        center.y + minLength * sin(minAngle).toFloat()
                    ),
                    strokeWidth = 2.5.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Second Hand (accent colored, pointing towards 45)
                val secAngle = Math.toRadians((45.0 * 6.0) - 90.0)
                val secLength = radius * 0.85f
                drawLine(
                    color = accentColor,
                    start = center,
                    end = Offset(
                        center.x + secLength * cos(secAngle).toFloat(),
                        center.y + secLength * sin(secAngle).toFloat()
                    ),
                    strokeWidth = 1.5.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Center Pip
                drawCircle(color = accentColor, radius = 4.dp.toPx(), center = center)
                drawCircle(color = Color.Black, radius = 1.5.dp.toPx(), center = center)
            }
        }

        // Digital Companion Column
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "10:30",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = if (language == AppLanguage.GEORGIAN) "თბილისი • GMT+4" else "Tbilisi • GMT+4",
                color = accentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = if (language == AppLanguage.GEORGIAN) "მზე ჩადის 18:14" else "Sunset at 18:14",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun DualWorldClockComposable(
    accentColor: Color,
    is24h: Boolean,
    language: AppLanguage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // City 1: Tbilisi
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "თბილისი" else "Tbilisi",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = if (is24h) "10:30" else "10:30 AM",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (language == AppLanguage.GEORGIAN) "დღეს • +0 სთ" else "Today • Local",
                color = accentColor,
                fontSize = 10.sp
            )
        }

        // Divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(60.dp)
                .background(Color.White.copy(alpha = 0.15f))
        )

        // City 2: Seoul (Samsung HQ)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = Color(0xFF5CE1E6),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "სეული" else "Seoul",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = if (is24h) "15:30" else "03:30 PM",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (language == AppLanguage.GEORGIAN) "დღეს • +5 სთ" else "Today • +5h",
                color = Color(0xFF5CE1E6),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun LinearPowerDeckComposable(
    accentColor: Color,
    batteryInfo: DeviceBatteryTelemetry,
    isCharging: Boolean,
    language: AppLanguage
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isCharging) Icons.Default.Bolt else Icons.Default.PhoneAndroid,
                    contentDescription = null,
                    tint = if (isCharging) Color(0xFF48CFAD) else accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Samsung S24 Ultra Power Deck",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "${batteryInfo.percentage}%",
                color = if (isCharging) Color(0xFF48CFAD) else accentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.12f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(batteryInfo.percentage / 100f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(accentColor, if (isCharging) Color(0xFF48CFAD) else accentColor.copy(alpha = 0.8f))
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Metrics row: S-Pen status + Runtime estimate
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "S-Pen",
                    tint = Color(0xFFFED766),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "S-Pen: 100% ჩასმულია" else "S-Pen: 100% Inserted",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 10.sp
                )
            }

            Text(
                text = if (language == AppLanguage.GEORGIAN)
                    "დარჩენილია: ~24 სთ 12 წთ"
                else
                    "Est: ~24h 12m left",
                color = accentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun CompactBatteryPillComposable(
    accentColor: Color,
    batteryInfo: DeviceBatteryTelemetry,
    isCharging: Boolean,
    language: AppLanguage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (isCharging) Color(0xFF48CFAD).copy(alpha = 0.2f) else accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCharging) Icons.Default.Bolt else Icons.Default.BatteryChargingFull,
                    contentDescription = null,
                    tint = if (isCharging) Color(0xFF48CFAD) else accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "${batteryInfo.percentage}%",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${batteryInfo.voltageMv} mV • ${batteryInfo.temperatureC}°C",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 10.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.1f))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = if (isCharging) "Super Fast 2.0" else "Li-ion Optimal",
                color = if (isCharging) Color(0xFF48CFAD) else accentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
