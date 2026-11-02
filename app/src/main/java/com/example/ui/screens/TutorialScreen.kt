package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.TutorialData
import com.example.model.TutorialStep
import com.example.ui.theme.AestheticAccent
import com.example.ui.theme.AestheticAccentSecondary
import com.example.ui.theme.S24BorderDark
import com.example.ui.theme.S24CardDark
import com.example.ui.theme.S24SurfaceDark
import com.example.ui.theme.S24TextMuted
import com.example.ui.theme.S24TextPrimary
import com.example.ui.theme.S24TextSecondary
import com.example.ui.theme.S24TitaniumBlack
import kotlinx.coroutines.launch

@Composable
fun TutorialScreen(
    language: AppLanguage,
    modifier: Modifier = Modifier,
    onNavigateToStudio: () -> Unit = {},
    onNavigateToApply: () -> Unit = {},
    onNavigateToGallery: () -> Unit = {}
) {
    val steps = TutorialData.steps
    val pagerState = rememberPagerState(pageCount = { steps.size })
    val coroutineScope = rememberCoroutineScope()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(S24TitaniumBlack)
            .testTag("tutorial_screen_root")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Navigation & Step Quick-Jump Strip
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(S24SurfaceDark.copy(alpha = 0.95f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(AestheticAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Smartphone,
                                contentDescription = null,
                                tint = AestheticAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "Samsung S24 Ultra ტუტორიალი" else "S24 Ultra One UI 6.1 Guide",
                                color = S24TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (language == AppLanguage.GEORGIAN) "ვერტიკალური გზამკვლევი • ნაბიჯი ${pagerState.currentPage + 1} / ${steps.size}"
                                else "Vertical Step Guide • Step ${pagerState.currentPage + 1} of ${steps.size}",
                                color = AestheticAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Progress indicator pill
                    Surface(
                        color = AestheticAccent.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, AestheticAccent.copy(alpha = 0.35f))
                    ) {
                        Text(
                            text = "${((pagerState.currentPage + 1) * 100) / steps.size}%",
                            color = AestheticAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quick jump step chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    itemsIndexed(steps) { index, step ->
                        val isSelected = pagerState.currentPage == index
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            label = {
                                Text(
                                    text = "${index + 1}. ${step.tag}",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = S24CardDark,
                                labelColor = S24TextSecondary,
                                selectedContainerColor = AestheticAccent,
                                selectedLabelColor = Color.Black
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) AestheticAccent else S24BorderDark,
                                selectedBorderColor = AestheticAccent
                            ),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("tutorial_step_chip_${step.id}")
                        )
                    }
                }
            }

            // Main Vertical Pager Component
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("tutorial_vertical_pager"),
                    beyondViewportPageCount = 1
                ) { pageIndex ->
                    val step = steps[pageIndex]
                    TutorialPageItem(
                        step = step,
                        language = language,
                        currentPage = pageIndex,
                        totalPages = steps.size,
                        onActionClicked = {
                            when (step.actionRoute) {
                                "studio" -> onNavigateToStudio()
                                "apply" -> onNavigateToApply()
                                "gallery" -> onNavigateToGallery()
                                else -> onNavigateToStudio()
                            }
                        }
                    )
                }

                // Vertical Page Progress Rail on Right Edge
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    steps.forEachIndexed { idx, _ ->
                        val isCurrent = pagerState.currentPage == idx
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(if (isCurrent) 22.dp else 8.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (isCurrent) AestheticAccent
                                    else S24TextMuted.copy(alpha = 0.4f)
                                )
                                .clickable {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(idx)
                                    }
                                }
                        )
                    }
                }
            }

            // Bottom Floating Navigation Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tutorial_bottom_bar"),
                color = S24SurfaceDark,
                tonalElevation = 6.dp,
                border = BorderStroke(1.dp, S24BorderDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Previous Step Button
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                if (pagerState.currentPage > 0) {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }
                        },
                        enabled = pagerState.currentPage > 0,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = S24TextPrimary,
                            disabledContentColor = S24TextMuted
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (pagerState.currentPage > 0) S24BorderDark else Color.Transparent
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("tutorial_prev_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Step",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "წინა" else "Prev",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Swipe hint indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.offset(y = bounceOffset.dp)
                    ) {
                        Icon(
                            imageVector = if (pagerState.currentPage < steps.size - 1) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            tint = AestheticAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = if (language == AppLanguage.GEORGIAN)
                                (if (pagerState.currentPage < steps.size - 1) "ჩამოფურცლეთ" else "დასრულდა")
                            else
                                (if (pagerState.currentPage < steps.size - 1) "Swipe vertical" else "Completed"),
                            color = S24TextSecondary,
                            fontSize = 10.sp
                        )
                    }

                    // Next / Finish Step Button
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (pagerState.currentPage < steps.size - 1) {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                } else {
                                    // Finished tutorial, jump to Studio or Setups
                                    onNavigateToStudio()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AestheticAccent,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("tutorial_next_button")
                    ) {
                        Text(
                            text = if (pagerState.currentPage < steps.size - 1) {
                                if (language == AppLanguage.GEORGIAN) "შემდეგი" else "Next"
                            } else {
                                if (language == AppLanguage.GEORGIAN) "სტუდიაში ცდა" else "Try Studio"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = if (pagerState.currentPage < steps.size - 1) Icons.AutoMirrored.Filled.ArrowForward else Icons.Default.Tune,
                            contentDescription = "Next Step",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TutorialPageItem(
    step: TutorialStep,
    language: AppLanguage,
    currentPage: Int,
    totalPages: Int,
    onActionClicked: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Step Hero Imagery Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("tutorial_image_card_${step.id}"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = S24CardDark),
            border = BorderStroke(1.dp, S24BorderDark)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                // Step Image
                if (step.imageRes != null) {
                    Image(
                        painter = painterResource(id = step.imageRes),
                        contentDescription = if (language == AppLanguage.GEORGIAN) step.titleGe else step.titleEn,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Aesthetic gradient fallback with icon
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF1E1738), Color(0xFF131728))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Smartphone,
                            contentDescription = null,
                            tint = AestheticAccent,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Dark gradient overlay for readability at top & bottom
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.55f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Top Badge: Device & Tag
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.75f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, AestheticAccent.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(AestheticAccent)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "S24 Ultra • One UI 6.1",
                                color = S24TextPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Surface(
                        color = AestheticAccent,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Step ${step.id}/$totalPages",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Bottom Tag label inside image
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(AestheticAccentSecondary.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AestheticAccentSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = step.tag,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Title and Summary Header
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (language == AppLanguage.GEORGIAN) step.titleGe else step.titleEn,
                color = S24TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (language == AppLanguage.GEORGIAN) step.shortSummaryGe else step.shortSummaryEn,
                color = S24TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }

        // Text Instructions Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = S24CardDark),
            border = BorderStroke(1.dp, S24BorderDark)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.GEORGIAN) "ინსტრუქციები ნაბიჯ-ნაბიჯ:" else "Step-by-Step Instructions:",
                    color = AestheticAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                val instructions = if (language == AppLanguage.GEORGIAN) step.instructionsGe else step.instructionsEn
                instructions.forEachIndexed { index, line ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(AestheticAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = AestheticAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = line,
                            color = S24TextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Pro-Tip Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1A11)),
            border = BorderStroke(1.dp, Color(0xFFFBBF24).copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFBBF24).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "S24 Ultra ექსპერტის რჩევა" else "S24 Ultra Expert Tip",
                        color = Color(0xFFFBBF24),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) step.tipGe else step.tipEn,
                        color = Color(0xFFFEF3C7),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Action CTA inside step card
        FilledTonalButton(
            onClick = onActionClicked,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = S24SurfaceDark,
                contentColor = AestheticAccent
            ),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, AestheticAccent.copy(alpha = 0.4f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("tutorial_action_button_${step.id}")
        ) {
            Icon(
                imageVector = when (step.actionRoute) {
                    "apply" -> Icons.Default.Wallpaper
                    "studio" -> Icons.Default.Tune
                    else -> Icons.Default.OpenInNew
                },
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = when (step.actionRoute) {
                    "apply" -> if (language == AppLanguage.GEORGIAN) "ფონის დაყენება ამ აპიდან" else "Apply Wallpaper Now"
                    "studio" -> if (language == AppLanguage.GEORGIAN) "გახსნა სტუდიაში და მორგება" else "Try in Customizer Studio"
                    "gallery" -> if (language == AppLanguage.GEORGIAN) "თემების კოლექციის ნახვა" else "Explore 9 Theme Sets"
                    else -> if (language == AppLanguage.GEORGIAN) "გახსნა სტუდიაში" else "Open in Studio"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
