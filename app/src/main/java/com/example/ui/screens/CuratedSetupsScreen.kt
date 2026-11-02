package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.CuratedSetup
import com.example.model.CuratedSetupsData
import com.example.model.LockScreenConfig
import com.example.model.ThemeItem
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
fun CuratedSetupsScreen(
    language: AppLanguage,
    onSelectSetupForCustomizer: (ThemeItem, LockScreenConfig) -> Unit,
    onQuickApplySetup: (ThemeItem, LockScreenConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(S24TitaniumBlack),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Banner Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF231B42),
                                Color(0xFF14172B)
                            )
                        )
                    )
                    .border(1.dp, AestheticAccent.copy(alpha = 0.35f), RoundedCornerShape(22.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AestheticAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "S24 Ultra დიზაინერის რეკომენდაციები" else "S24 Ultra Curated Aesthetic Setups",
                            color = S24TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (language == AppLanguage.GEORGIAN)
                            "შერჩეული კომბინაციები: ფონი + საათის ტიპოგრაფია + ვიჯეტების განლაგება + ფერთა პალიტრა, შთაგონებული iThemeDesign-ის ესთეტიკით."
                        else
                            "Harmonious combinations of wallpaper, clock typography, widget hierarchy, and matching color swatches tailored for S24 Ultra.",
                        color = S24TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Interactive Widgets Showcase Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Widgets,
                    contentDescription = null,
                    tint = AestheticAccent,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "3 ინტერაქციული ვიჯეტი (Live Widgets)" else "3 Interactive Aesthetic Widgets",
                        color = S24TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "შეეხეთ ვიჯეტებს რეჟიმების, შრიფტისა და ანიმაციების შესამოწმებლად" else "Tap each widget to test modes, typography, and live animations",
                        color = S24TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // 1. Clock Widget
        item {
            com.example.ui.components.AestheticClockWidget(
                language = language,
                accentColor = AestheticAccent
            )
        }

        // 2. Weather Widget
        item {
            com.example.ui.components.AestheticWeatherWidget(
                language = language,
                accentColor = AestheticAccent
            )
        }

        // 3. Battery Widget
        item {
            com.example.ui.components.AestheticBatteryWidget(
                language = language,
                accentColor = AestheticAccent
            )
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FormatPaint,
                    contentDescription = null,
                    tint = Color(0xFF5CE1E6),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "სრული ლოქსქრინ დიზაინები (Lock Screen Layouts)" else "Complete Lock Screen Layouts",
                    color = S24TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Setups list
        items(CuratedSetupsData.setups, key = { it.id }) { setup ->
            CuratedSetupCard(
                setup = setup,
                language = language,
                onCustomize = { onSelectSetupForCustomizer(setup.theme, setup.config) },
                onApply = { onQuickApplySetup(setup.theme, setup.config) }
            )
        }
    }
}

@Composable
private fun CuratedSetupCard(
    setup: CuratedSetup,
    language: AppLanguage,
    onCustomize: () -> Unit,
    onApply: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("curated_setup_card_${setup.id}"),
        colors = CardDefaults.cardColors(containerColor = S24CardDark),
        shape = RoundedCornerShape(22.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(S24BorderDark, S24BorderDark)))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) setup.titleGe else setup.titleEn,
                        color = S24TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) setup.aestheticVibeGe else setup.aestheticVibeEn,
                        color = S24TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Phone Mockup & Color Swatches in Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Interactive S24 Ultra Phone Frame
                Box(
                    modifier = Modifier
                        .width(135.dp)
                        .height(280.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onCustomize)
                ) {
                    S24UltraFrame(
                        theme = setup.theme,
                        config = setup.config,
                        language = language,
                        previewMode = ScreenPreviewMode.LOCK_SCREEN,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Palette & Synergy Details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(280.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        // Palette Section
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ColorLens,
                                contentDescription = null,
                                tint = AestheticAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "ფერთა ჰარმონია" else "Color Palette",
                                color = S24TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Swatches
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            setup.colorPaletteList.forEachIndexed { idx, color ->
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Typography reasoning
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.FormatPaint,
                                contentDescription = null,
                                tint = Color(0xFF5CE1E6),
                                modifier = Modifier
                                    .size(13.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) setup.typographyReasoningGe else setup.typographyReasoningEn,
                                color = S24TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Widget integration reasoning
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Widgets,
                                contentDescription = null,
                                tint = Color(0xFFFFD166),
                                modifier = Modifier
                                    .size(13.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) setup.widgetIntegrationReasoningGe else setup.widgetIntegrationReasoningEn,
                                color = S24TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // AOD Tip
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black.copy(alpha = 0.3f))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "AOD: ${setup.aodRecommendationGe}" else "AOD: ${setup.aodRecommendationEn}",
                            color = Color(0xFFDEDDFF),
                            fontSize = 10.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onCustomize,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("customize_setup_button_${setup.id}"),
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
                        text = if (language == AppLanguage.GEORGIAN) "მორგება" else "Customize",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = onApply,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("apply_setup_button_${setup.id}"),
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
                        text = if (language == AppLanguage.GEORGIAN) "ამ ნაკრების დაყენება" else "Apply Setup",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
