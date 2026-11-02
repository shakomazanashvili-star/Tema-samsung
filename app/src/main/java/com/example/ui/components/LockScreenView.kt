package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ClockStyle
import com.example.model.LockScreenConfig
import com.example.model.ThemeItem
import com.example.model.WidgetType

@Composable
fun LockScreenView(
    theme: ThemeItem,
    config: LockScreenConfig,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val dateText = if (language == AppLanguage.GEORGIAN) "შაბათი, 31 ოქტომბერი" else "Saturday, October 31"
    val swipeText = if (language == AppLanguage.GEORGIAN) "გასახსნელად გადაუსვით" else "Swipe to unlock"

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Main Lockscreen Content (Centered upper half)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp, start = 18.dp, end = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Date Header
            Text(
                text = dateText,
                color = config.clockColor.copy(alpha = 0.88f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Clock
            LockScreenClock(
                style = config.clockStyle,
                color = config.clockColor,
                sizeMultiplier = config.clockSizeMultiplier
            )

            Spacer(modifier = Modifier.height(10.dp))

            // One UI 6.1 Lock Screen Widgets Row
            if (config.activeWidgets.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.22f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    config.activeWidgets.forEach { widgetType ->
                        LockScreenWidget(
                            type = widgetType,
                            color = config.clockColor,
                            language = language
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notification Capsule
            if (config.showNotificationPill) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.28f))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(theme.accentColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "iTheme Design • 9 ფონის ნაკრები" else "iTheme Design • 9 Aesthetic Sets",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        // Bottom Controls: Swipe bar & Shortcuts
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Swipe prompt text
            Text(
                text = swipeText,
                color = Color.White.copy(alpha = pulseAlpha),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom action shortcuts (Flashlight & Camera)
            if (config.showShortcuts) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Flashlight shortcut
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.35f))
                            .border(1.dp, Color.White.copy(alpha = 0.18f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashlightOn,
                            contentDescription = "Flashlight",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Samsung Bottom Indicator Bar
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White.copy(alpha = 0.6f))
                    )

                    // Camera shortcut
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.35f))
                            .border(1.dp, Color.White.copy(alpha = 0.18f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Camera",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LockScreenClock(
    style: ClockStyle,
    color: Color,
    sizeMultiplier: Float = 1.0f
) {
    when (style) {
        ClockStyle.SERIF_BOLD -> {
            Text(
                text = "10:30",
                color = color,
                fontSize = (72 * sizeMultiplier).sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                letterSpacing = (-2).sp,
                lineHeight = (72 * sizeMultiplier).sp
            )
        }
        ClockStyle.CLEAN_SANS -> {
            Text(
                text = "10:30",
                color = color,
                fontSize = (68 * sizeMultiplier).sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = (-1).sp
            )
        }
        ClockStyle.STACKED_NUMBERS -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "10",
                    color = color,
                    fontSize = (56 * sizeMultiplier).sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    lineHeight = (50 * sizeMultiplier).sp
                )
                Text(
                    text = "30",
                    color = color,
                    fontSize = (56 * sizeMultiplier).sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    lineHeight = (50 * sizeMultiplier).sp
                )
            }
        }
        ClockStyle.THIN_ROMAN -> {
            Text(
                text = "10:30",
                color = color,
                fontSize = (68 * sizeMultiplier).sp,
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Serif,
                letterSpacing = 2.sp
            )
        }
        ClockStyle.HANDWRITTEN -> {
            Text(
                text = "10:30",
                color = color,
                fontSize = (62 * sizeMultiplier).sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Cursive,
                fontStyle = FontStyle.Italic,
                letterSpacing = 1.sp
            )
        }
        ClockStyle.PILL_DIGITAL -> {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black.copy(alpha = 0.35f))
                    .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(24.dp))
                    .padding(horizontal = 24.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "10:30",
                    color = color,
                    fontSize = (52 * sizeMultiplier).sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
fun LockScreenWidget(
    type: WidgetType,
    color: Color,
    language: AppLanguage
) {
    val widgetColor = color.copy(alpha = 0.92f)

    when (type) {
        WidgetType.BATTERY_RING -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BatteryFull,
                    contentDescription = "Battery",
                    tint = widgetColor,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "98%",
                    color = widgetColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        WidgetType.WEATHER -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = "Weather",
                    tint = Color(0xFFFFD166),
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "22°",
                    color = widgetColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        WidgetType.STEP_COUNTER -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsWalk,
                    contentDescription = "Steps",
                    tint = Color(0xFF48CFAD),
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "7.8k",
                    color = widgetColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        WidgetType.CALENDAR_EVENT -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Event",
                    tint = widgetColor,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "14:00",
                    color = widgetColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        WidgetType.MUSIC_PLAYER -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Music",
                    tint = widgetColor,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "M83",
                    color = widgetColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        WidgetType.QUOTE_TEXT -> {
            Text(
                text = if (language == AppLanguage.GEORGIAN) "ესთეტიკა" else "Aesthetic",
                color = widgetColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
