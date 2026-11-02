package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.AestheticAccent

// =========================================================================
// 1. AESTHETIC INTERACTIVE CLOCK WIDGET
// =========================================================================

enum class ClockFontMode {
    EDITORIAL_SERIF,
    CLEAN_SANS,
    MINIMAL_ROMAN
}

@Composable
fun AestheticClockWidget(
    accentColor: Color = AestheticAccent,
    textColor: Color = Color.White,
    language: AppLanguage = AppLanguage.GEORGIAN,
    modifier: Modifier = Modifier
) {
    var fontMode by remember { mutableStateOf(ClockFontMode.EDITORIAL_SERIF) }
    var is24HourFormat by remember { mutableStateOf(true) }
    var showSeconds by remember { mutableStateOf(false) }

    val formattedTime = if (is24HourFormat) {
        if (showSeconds) "10:30:45" else "10:30"
    } else {
        if (showSeconds) "10:30:45 AM" else "10:30 AM"
    }

    val dateHeader = if (language == AppLanguage.GEORGIAN) {
        "შაბათი, 31 ოქტომბერი • თბილისი"
    } else {
        "Saturday, Oct 31 • Sunny"
    }

    val dayProgress = 0.44f // 10:30 out of 24h

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Color.Black.copy(alpha = 0.42f))
            .border(
                width = 1.2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.22f),
                        accentColor.copy(alpha = 0.35f),
                        Color.White.copy(alpha = 0.08f)
                    )
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                // Cycle font style on tap
                fontMode = when (fontMode) {
                    ClockFontMode.EDITORIAL_SERIF -> ClockFontMode.CLEAN_SANS
                    ClockFontMode.CLEAN_SANS -> ClockFontMode.MINIMAL_ROMAN
                    ClockFontMode.MINIMAL_ROMAN -> ClockFontMode.EDITORIAL_SERIF
                }
            }
            .testTag("interactive_clock_widget")
            .padding(20.dp)
    ) {
        Column {
            // Header Row: Date & Format Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateHeader,
                    color = textColor.copy(alpha = 0.75f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.4.sp
                )

                // 12h / 24h toggle capsule
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.22f))
                        .clickable { is24HourFormat = !is24HourFormat }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (is24HourFormat) "24H" else "12H",
                        color = accentColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Main Time Display
            AnimatedContent(
                targetState = formattedTime to fontMode,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "clock_anim"
            ) { (time, font) ->
                when (font) {
                    ClockFontMode.EDITORIAL_SERIF -> {
                        Text(
                            text = time,
                            color = textColor,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = (-1.5).sp,
                            lineHeight = 52.sp
                        )
                    }
                    ClockFontMode.CLEAN_SANS -> {
                        Text(
                            text = time,
                            color = textColor,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif,
                            letterSpacing = (-0.5).sp,
                            lineHeight = 48.sp
                        )
                    }
                    ClockFontMode.MINIMAL_ROMAN -> {
                        Text(
                            text = time,
                            color = textColor,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 3.sp,
                            lineHeight = 48.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Day Progress Bar (Minimalist aesthetic line)
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "დღის პროგრესი" else "Day Elapsed",
                        color = textColor.copy(alpha = 0.5f),
                        fontSize = 10.sp
                    )
                    Text(
                        text = "${(dayProgress * 100).toInt()}%",
                        color = accentColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(1.5.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(dayProgress)
                            .height(3.dp)
                            .clip(RoundedCornerShape(1.5.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(accentColor.copy(alpha = 0.6f), accentColor)
                                )
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Tap hint
            Text(
                text = if (language == AppLanguage.GEORGIAN)
                    "შეეხეთ შრიფტის შესაცვლელად (Serif / Sans / Roman)"
                else
                    "Tap anywhere to switch typography (Serif / Sans / Roman)",
                color = textColor.copy(alpha = 0.45f),
                fontSize = 9.sp,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

// =========================================================================
// 2. AESTHETIC INTERACTIVE WEATHER WIDGET
// =========================================================================

enum class WeatherCondition(
    val conditionNameEn: String,
    val conditionNameGe: String,
    val icon: ImageVector,
    val tempC: Int,
    val tempF: Int,
    val iconTint: Color
) {
    SUNNY("Sunny", "მზიანი", Icons.Default.WbSunny, 22, 72, Color(0xFFFFD166)),
    RAINY("Gentle Rain", "მსუბუქი წვიმა", Icons.Default.WaterDrop, 17, 63, Color(0xFF5CE1E6)),
    CLOUDY("Partly Cloudy", "ნაწილობრივ ღრუბლიანი", Icons.Default.Cloud, 20, 68, Color(0xFFD6CFC7)),
    NIGHT_CLEAR("Starlit Night", "ვარსკვლავიანი ღამე", Icons.Default.NightsStay, 15, 59, Color(0xFFC084FC))
}

@Composable
fun AestheticWeatherWidget(
    accentColor: Color = AestheticAccent,
    textColor: Color = Color.White,
    language: AppLanguage = AppLanguage.GEORGIAN,
    modifier: Modifier = Modifier
) {
    val conditions = WeatherCondition.values()
    var conditionIndex by remember { mutableIntStateOf(0) }
    var useCelsius by remember { mutableStateOf(true) }

    val current = conditions[conditionIndex]
    val displayedTemp = if (useCelsius) "${current.tempC}°C" else "${current.tempF}°F"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Color.Black.copy(alpha = 0.42f))
            .border(
                width = 1.2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f),
                        current.iconTint.copy(alpha = 0.35f),
                        Color.White.copy(alpha = 0.08f)
                    )
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .clickable {
                // Cycle weather state on tap to preview different aesthetic vibes
                conditionIndex = (conditionIndex + 1) % conditions.size
            }
            .testTag("interactive_weather_widget")
            .padding(20.dp)
    ) {
        Column {
            // Top Row: Location & Temp unit toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(current.iconTint)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "თბილისი • ცოცხალი ამინდი" else "Tbilisi • Live Weather",
                        color = textColor.copy(alpha = 0.75f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // °C / °F toggle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .clickable { useCelsius = !useCelsius }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (useCelsius) "°C" else "°F",
                        color = current.iconTint,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Weather Info: Large Icon + Big Temp + Condition
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    AnimatedContent(
                        targetState = displayedTemp,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "temp_anim"
                    ) { temp ->
                        Text(
                            text = temp,
                            color = textColor,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            letterSpacing = (-1).sp
                        )
                    }

                    Text(
                        text = if (language == AppLanguage.GEORGIAN) current.conditionNameGe else current.conditionNameEn,
                        color = textColor.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Glowing condition icon
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(current.iconTint.copy(alpha = 0.16f))
                        .border(1.dp, current.iconTint.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = current.icon,
                        contentDescription = current.conditionNameEn,
                        tint = current.iconTint,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Environmental metrics row (Humidity, Wind, UV)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WeatherMetricPill(
                    icon = Icons.Default.WaterDrop,
                    label = if (language == AppLanguage.GEORGIAN) "ტენიანობა" else "Humidity",
                    value = "58%",
                    tint = Color(0xFF67E8F9),
                    textColor = textColor
                )

                WeatherMetricPill(
                    icon = Icons.Default.Air,
                    label = if (language == AppLanguage.GEORGIAN) "ქარი" else "Wind",
                    value = "12 კმ/სთ",
                    tint = Color(0xFFA7F3D0),
                    textColor = textColor
                )

                WeatherMetricPill(
                    icon = Icons.Default.WbSunny,
                    label = "UV",
                    value = "3 (ზომიერი)",
                    tint = Color(0xFFFED7AA),
                    textColor = textColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == AppLanguage.GEORGIAN) "შეეხეთ ამინდის მდგომარეობის შესაცვლელად" else "Tap widget to cycle weather conditions",
                color = textColor.copy(alpha = 0.45f),
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun WeatherMetricPill(
    icon: ImageVector,
    label: String,
    value: String,
    tint: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.07f))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(13.dp)
        )
        Column {
            Text(
                text = label,
                color = textColor.copy(alpha = 0.5f),
                fontSize = 8.sp
            )
            Text(
                text = value,
                color = textColor.copy(alpha = 0.9f),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// =========================================================================
// 3. AESTHETIC INTERACTIVE MULTI-DEVICE BATTERY WIDGET (S24 Ultra Ecosystem)
// =========================================================================

data class DeviceBattery(
    val nameEn: String,
    val nameGe: String,
    val icon: ImageVector,
    val percentage: Float, // 0.0f - 1.0f
    val isCharging: Boolean,
    val accent: Color
)

@Composable
fun AestheticBatteryWidget(
    accentColor: Color = AestheticAccent,
    textColor: Color = Color.White,
    language: AppLanguage = AppLanguage.GEORGIAN,
    modifier: Modifier = Modifier
) {
    // Interactive states for devices
    var phoneCharging by remember { mutableStateOf(false) }
    var budsCharging by remember { mutableStateOf(true) }
    var watchCharging by remember { mutableStateOf(false) }

    val devices = listOf(
        DeviceBattery(
            nameEn = "S24 Ultra",
            nameGe = "S24 Ultra",
            icon = Icons.Default.Smartphone,
            percentage = 0.94f,
            isCharging = phoneCharging,
            accent = Color(0xFF48CFAD)
        ),
        DeviceBattery(
            nameEn = "Galaxy Watch",
            nameGe = "Galaxy Watch",
            icon = Icons.Default.Watch,
            percentage = 0.82f,
            isCharging = watchCharging,
            accent = Color(0xFF5CE1E6)
        ),
        DeviceBattery(
            nameEn = "Galaxy Buds",
            nameGe = "Galaxy Buds",
            icon = Icons.Default.Headphones,
            percentage = 1.0f,
            isCharging = budsCharging,
            accent = Color(0xFFC084FC)
        )
    )

    // Pulse animation for charging state
    val infiniteTransition = rememberInfiniteTransition(label = "charging_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Color.Black.copy(alpha = 0.42f))
            .border(
                width = 1.2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f),
                        accentColor.copy(alpha = 0.35f),
                        Color.White.copy(alpha = 0.08f)
                    )
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .clickable {
                // Toggle phone charging simulation on tap
                phoneCharging = !phoneCharging
            }
            .testTag("interactive_battery_widget")
            .padding(20.dp)
    ) {
        Column {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "ეკოსისტემის ელემენტი" else "Galaxy Battery Rings",
                    color = textColor.copy(alpha = 0.75f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (phoneCharging || budsCharging || watchCharging) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Charging",
                            tint = Color(0xFF48CFAD),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "სწრაფი დატენვა" else "Fast Charging",
                            color = Color(0xFF48CFAD),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3 Circular Battery Rings in Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                devices.forEach { device ->
                    BatteryGaugeRing(
                        device = device,
                        language = language,
                        textColor = textColor,
                        pulseScale = if (device.isCharging) pulseScale else 1.0f
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.GEORGIAN)
                    "შეეხეთ ტელეფონის დატენვის სიმულაციისთვის (Fast Charging Toggle)"
                else
                    "Tap widget to toggle phone fast-charging simulation",
                color = textColor.copy(alpha = 0.45f),
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun BatteryGaugeRing(
    device: DeviceBattery,
    language: AppLanguage,
    textColor: Color,
    pulseScale: Float
) {
    val animatedProgress by animateFloatAsState(
        targetValue = device.percentage,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier.size(62.dp),
            contentAlignment = Alignment.Center
        ) {
            // Circular arc canvas
            Canvas(modifier = Modifier.size(62.dp)) {
                val strokeWidth = 5.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2f
                val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
                val arcSize = Size(radius * 2, radius * 2)

                // Track (Background Ring)
                drawArc(
                    color = Color.White.copy(alpha = 0.12f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Active Gradient Ring
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(device.accent.copy(alpha = 0.7f), device.accent)
                    ),
                    startAngle = -90f,
                    sweepAngle = animatedProgress * 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // Center Content: Device Icon or Bolt if charging
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (device.isCharging) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Charging",
                        tint = device.accent,
                        modifier = Modifier.size((18 * pulseScale).dp)
                    )
                } else {
                    Icon(
                        imageVector = device.icon,
                        contentDescription = device.nameEn,
                        tint = textColor.copy(alpha = 0.9f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Percentage & Device Label
        Text(
            text = "${(device.percentage * 100).toInt()}%",
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = if (language == AppLanguage.GEORGIAN) device.nameGe else device.nameEn,
            color = textColor.copy(alpha = 0.65f),
            fontSize = 10.sp
        )
    }
}
