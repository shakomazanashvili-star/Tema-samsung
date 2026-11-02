package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.AestheticAccent
import com.example.ui.theme.LocalAestheticColors
import com.example.ui.theme.S24BorderDark
import com.example.ui.theme.S24CardDark
import com.example.ui.theme.S24SurfaceDark
import com.example.ui.theme.S24TextMuted
import com.example.ui.theme.S24TextPrimary
import com.example.ui.theme.S24TextSecondary
import com.example.ui.theme.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    currentThemeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isEffectiveDark = when (currentThemeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemDark
    }

    val palette = LocalAestheticColors.current

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("settings_dialog"),
        containerColor = S24SurfaceDark,
        tonalElevation = 12.dp,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AestheticAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = AestheticAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "პარამეტრები" else "Settings",
                            color = S24TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "ეკრანის თემა და ფერები" else "Display theme & appearance",
                            color = S24TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = S24TextSecondary
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Section Title: Theme Mode
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "ინტერფეისის თემა" else "Interface Appearance",
                    color = AestheticAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Quick Switch Row (One UI 6.1 Styled)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = S24CardDark),
                    border = BorderStroke(1.dp, S24BorderDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (isEffectiveDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                tint = AestheticAccent,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.GEORGIAN) "მუქი რეჟიმი" else "Dark Mode",
                                    color = S24TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (isEffectiveDark) {
                                        if (language == AppLanguage.GEORGIAN) "S24 Ultra OLED შავი ფერი ჩართულია" else "AMOLED deep black enabled"
                                    } else {
                                        if (language == AppLanguage.GEORGIAN) "ნათელი რეჟიმი ჩართულია" else "Porcelain light mode enabled"
                                    },
                                    color = S24TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Switch(
                            checked = isEffectiveDark,
                            onCheckedChange = { isChecked ->
                                onThemeModeChanged(if (isChecked) ThemeMode.DARK else ThemeMode.LIGHT)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AestheticAccent,
                                uncheckedThumbColor = S24TextSecondary,
                                uncheckedTrackColor = S24BorderDark
                            ),
                            modifier = Modifier.testTag("dark_mode_switch")
                        )
                    }
                }

                // 3 Discrete Theme Selection Options
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "რეჟიმის არჩევა" else "Select Mode",
                    color = S24TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                ThemeModeOptionRow(
                    title = if (language == AppLanguage.GEORGIAN) "მუქი რეჟიმი" else "Dark Mode",
                    subtitle = if (language == AppLanguage.GEORGIAN) "S24 Ultra OLED True Black • ენერგიის დაზოგვა" else "S24 Ultra AMOLED True Black • Battery Saver",
                    icon = Icons.Default.DarkMode,
                    isSelected = currentThemeMode == ThemeMode.DARK,
                    onClick = { onThemeModeChanged(ThemeMode.DARK) },
                    testTag = "theme_option_dark"
                )

                Spacer(modifier = Modifier.height(6.dp))

                ThemeModeOptionRow(
                    title = if (language == AppLanguage.GEORGIAN) "ნათელი რეჟიმი" else "Light Mode",
                    subtitle = if (language == AppLanguage.GEORGIAN) "Titanium White • სუფთა კონტრასტი დღის შუქზე" else "Titanium Porcelain • High daylight contrast",
                    icon = Icons.Default.LightMode,
                    isSelected = currentThemeMode == ThemeMode.LIGHT,
                    onClick = { onThemeModeChanged(ThemeMode.LIGHT) },
                    testTag = "theme_option_light"
                )

                Spacer(modifier = Modifier.height(6.dp))

                ThemeModeOptionRow(
                    title = if (language == AppLanguage.GEORGIAN) "სისტემური" else "System Default",
                    subtitle = if (language == AppLanguage.GEORGIAN) "მიჰყვება ტელეფონის One UI რეჟიმს" else "Matches your Galaxy One UI system theme",
                    icon = Icons.Default.SettingsBrightness,
                    isSelected = currentThemeMode == ThemeMode.SYSTEM,
                    onClick = { onThemeModeChanged(ThemeMode.SYSTEM) },
                    testTag = "theme_option_system"
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Live Active Palette Preview
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "აქტიური ფერთა პალიტრა" else "Active Color Palette",
                    color = AestheticAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = S24CardDark),
                    border = BorderStroke(1.dp, S24BorderDark)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            PaletteColorItem("Background", palette.background, language)
                            PaletteColorItem("Card", palette.card, language)
                            PaletteColorItem("Accent", palette.accent, language)
                            PaletteColorItem("Text", palette.textPrimary, language)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // S24 Ultra Display Optimization Note
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AestheticAccent.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, AestheticAccent.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Smartphone,
                            contentDescription = null,
                            tint = AestheticAccent,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "Samsung S24 Ultra ოპტიმიზაცია" else "S24 Ultra Display Optimization",
                                color = S24TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (language == AppLanguage.GEORGIAN)
                                    "Dynamic AMOLED 2X ეკრანზე მუქი რეჟიმი თიშავს შავ პიქსელებს და ზოგავს ბატარეას. Corning Gorilla Armor ანტიარეკვლადი შუშა უზრუნველყოფს იდეალურ ხილვადობას ნათელ რეჟიმშიც."
                                else
                                    "Dynamic AMOLED 2X completely shuts off black pixels in Dark mode for maximum battery life. Corning Gorilla Armor glass ensures zero glare in Light mode.",
                                color = S24TextSecondary,
                                fontSize = 10.sp,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AestheticAccent,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.testTag("close_settings_confirm_button")
            ) {
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "დასრულება" else "Done",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    )
}

@Composable
private fun ThemeModeOptionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) AestheticAccent else S24BorderDark,
        label = "border_color"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) AestheticAccent.copy(alpha = 0.12f) else S24CardDark,
        label = "card_color"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) AestheticAccent else S24BorderDark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.Black else S24TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = title,
                        color = if (isSelected) AestheticAccent else S24TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                    Text(
                        text = subtitle,
                        color = S24TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = AestheticAccent,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun PaletteColorItem(
    name: String,
    color: Color,
    language: AppLanguage
) {
    val translated = when (name) {
        "Background" -> if (language == AppLanguage.GEORGIAN) "ფონი" else "Base"
        "Card" -> if (language == AppLanguage.GEORGIAN) "ბარათი" else "Card"
        "Accent" -> if (language == AppLanguage.GEORGIAN) "აქცენტი" else "Accent"
        else -> if (language == AppLanguage.GEORGIAN) "ტექსტი" else "Text"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(color)
                .border(1.dp, S24BorderDark, CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = translated,
            color = S24TextSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
