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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.model.TutorialData
import com.example.model.TutorialStep
import com.example.ui.theme.AestheticAccent
import com.example.ui.theme.S24BorderDark
import com.example.ui.theme.S24CardDark
import com.example.ui.theme.S24SurfaceDark
import com.example.ui.theme.S24TextMuted
import com.example.ui.theme.S24TextPrimary
import com.example.ui.theme.S24TextSecondary
import com.example.ui.theme.S24TitaniumBlack

@Composable
fun TutorialScreen(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(S24TitaniumBlack),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Tutorial Header Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1E1738),
                                Color(0xFF131728)
                            )
                        )
                    )
                    .border(1.dp, AestheticAccent.copy(alpha = 0.3f), RoundedCornerShape(22.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AestheticAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Smartphone,
                                contentDescription = null,
                                tint = AestheticAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) "Samsung Galaxy S24 Ultra ტუტორიალი" else "Samsung S24 Ultra Widgets Tutorial",
                            color = S24TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (language == AppLanguage.GEORGIAN)
                            "როგორ დავაყენოთ iThemeDesign-ის სტილის ესთეტიკური ლოქსქრინი, საათი და ვიჯეტები პირდაპირ Samsung One UI 6.1 სისტემაში ნაბიჯ-ნაბიჯ."
                        else
                            "Step-by-step guide on how to configure iThemeDesign aesthetic lockscreens, custom clocks, and widgets directly in One UI 6.1.",
                        color = S24TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Steps List
        items(TutorialData.steps, key = { it.id }) { step ->
            TutorialStepCard(step = step, language = language)
        }
    }
}

@Composable
private fun TutorialStepCard(
    step: TutorialStep,
    language: AppLanguage
) {
    var isExpanded by remember { mutableStateOf(step.id <= 2) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tutorial_step_${step.id}"),
        colors = CardDefaults.cardColors(containerColor = S24CardDark),
        shape = RoundedCornerShape(18.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(S24BorderDark, S24BorderDark)))
    ) {
        Column(
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                            .clip(CircleShape)
                            .background(AestheticAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${step.id}",
                            color = AestheticAccent,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) step.titleGe else step.titleEn,
                            color = S24TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) step.shortSummaryGe else step.shortSummaryEn,
                            color = S24TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = S24TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    // Instruction points
                    val instructions = if (language == AppLanguage.GEORGIAN) step.instructionsGe else step.instructionsEn
                    instructions.forEachIndexed { idx, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AestheticAccent,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item,
                                color = S24TextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tip box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFBBF24).copy(alpha = 0.12f))
                            .border(1.dp, Color(0xFFFBBF24).copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.GEORGIAN) step.tipGe else step.tipEn,
                            color = Color(0xFFFEF3C7),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}
