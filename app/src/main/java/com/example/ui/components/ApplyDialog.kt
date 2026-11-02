package com.example.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.LockScreenConfig
import com.example.model.ThemeItem
import com.example.ui.theme.AestheticAccent
import com.example.ui.theme.S24BorderDark
import com.example.ui.theme.S24CardDark
import com.example.ui.theme.S24SurfaceDark
import com.example.ui.theme.S24TextPrimary
import com.example.ui.theme.S24TextSecondary
import com.example.util.WallpaperHelper
import com.example.util.WallpaperTarget
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyBottomSheet(
    theme: ThemeItem,
    config: LockScreenConfig,
    language: AppLanguage,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onApplied: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var activeAction by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = S24SurfaceDark,
        tonalElevation = 16.dp,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(42.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(S24BorderDark)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(theme.accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Wallpaper,
                        contentDescription = null,
                        tint = theme.accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) theme.titleGe else theme.titleEn,
                        color = S24TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (language == AppLanguage.GEORGIAN) "Samsung Galaxy S24 Ultra რეზოლუცია (QHD+)" else "Samsung Galaxy S24 Ultra QHD+ Native",
                        color = S24TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Options
            ApplyOptionTile(
                icon = Icons.Default.Lock,
                title = if (language == AppLanguage.GEORGIAN) "დაბლოკილ ეკრანზე დაყენება" else "Set on Lock Screen",
                subtitle = if (language == AppLanguage.GEORGIAN) "მხოლოდ Lock Screen-ის ფონის შეცვლა" else "Apply wallpaper only to Lock Screen",
                isLoading = isLoading && activeAction == "LOCK",
                tag = "apply_lock_button"
            ) {
                if (isLoading) return@ApplyOptionTile
                isLoading = true
                activeAction = "LOCK"
                scope.launch {
                    val result = WallpaperHelper.applyWallpaper(context, theme, WallpaperTarget.LOCK_SCREEN, config)
                    isLoading = false
                    if (result.isSuccess) {
                        val msg = if (language == AppLanguage.GEORGIAN) "ფონი წარმატებით დაყენდა ლოქსქრინზე!" else "Wallpaper applied to Lock Screen!"
                        onApplied(msg)
                        onDismiss()
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            ApplyOptionTile(
                icon = Icons.Default.PhoneAndroid,
                title = if (language == AppLanguage.GEORGIAN) "მთავარ ეკრანზე დაყენება" else "Set on Home Screen",
                subtitle = if (language == AppLanguage.GEORGIAN) "მხოლოდ Home Screen-ის ფონის შეცვლა" else "Apply wallpaper only to Home Screen",
                isLoading = isLoading && activeAction == "HOME",
                tag = "apply_home_button"
            ) {
                if (isLoading) return@ApplyOptionTile
                isLoading = true
                activeAction = "HOME"
                scope.launch {
                    val result = WallpaperHelper.applyWallpaper(context, theme, WallpaperTarget.HOME_SCREEN, config)
                    isLoading = false
                    if (result.isSuccess) {
                        val msg = if (language == AppLanguage.GEORGIAN) "ფონი წარმატებით დაყენდა მთავარ ეკრანზე!" else "Wallpaper applied to Home Screen!"
                        onApplied(msg)
                        onDismiss()
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            ApplyOptionTile(
                icon = Icons.Default.Wallpaper,
                title = if (language == AppLanguage.GEORGIAN) "ორივე ეკრანზე დაყენება" else "Set on Both Screens",
                subtitle = if (language == AppLanguage.GEORGIAN) "Lock Screen და Home Screen ერთდროულად" else "Synchronize Lock & Home Screen",
                isLoading = isLoading && activeAction == "BOTH",
                tag = "apply_both_button"
            ) {
                if (isLoading) return@ApplyOptionTile
                isLoading = true
                activeAction = "BOTH"
                scope.launch {
                    val result = WallpaperHelper.applyWallpaper(context, theme, WallpaperTarget.BOTH, config)
                    isLoading = false
                    if (result.isSuccess) {
                        val msg = if (language == AppLanguage.GEORGIAN) "ფონი წარმატებით დაყენდა ორივე ეკრანზე!" else "Wallpaper applied to Both Screens!"
                        onApplied(msg)
                        onDismiss()
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            ApplyOptionTile(
                icon = Icons.Default.Download,
                title = if (language == AppLanguage.GEORGIAN) "გალერეაში შენახვა (მაღალი ხარისხი)" else "Save to Gallery (QHD+ PNG)",
                subtitle = if (language == AppLanguage.GEORGIAN) "ფოტოს გადმოწერა Pictures/AestheticS24 საქაღალდეში" else "Download original image to device storage",
                isLoading = isLoading && activeAction == "DOWNLOAD",
                tag = "download_gallery_button"
            ) {
                if (isLoading) return@ApplyOptionTile
                isLoading = true
                activeAction = "DOWNLOAD"
                scope.launch {
                    val result = WallpaperHelper.saveToGallery(context, theme, config)
                    isLoading = false
                    if (result.isSuccess) {
                        val msg = if (language == AppLanguage.GEORGIAN) "ფონი შენახულია გალერეაში (Pictures/AestheticS24)!" else "Saved to Gallery (Pictures/AestheticS24)!"
                        onApplied(msg)
                        onDismiss()
                    }
                }
            }
        }
    }
}

@Composable
private fun ApplyOptionTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isLoading: Boolean,
    tag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(S24CardDark)
            .border(1.dp, S24BorderDark, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag(tag)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(AestheticAccent.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AestheticAccent,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = S24TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = S24TextSecondary,
                fontSize = 11.sp
            )
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = AestheticAccent,
                strokeWidth = 2.dp
            )
        }
    }
}
