package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentLanguage
import com.example.data.model.VerificationStatus
import com.example.ui.theme.DarkNavyBorder
import com.example.ui.theme.DarkNavyCard
import com.example.ui.theme.DarkNavySurface
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun StatusBadge(
    status: VerificationStatus,
    currentLanguage: ContentLanguage = ContentLanguage.HINDI,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when (status) {
        VerificationStatus.VERIFIED_LIVE -> Triple(
            Color(0x2210B981),
            NeonGreen,
            Icons.Default.CheckCircle
        )
        VerificationStatus.PUBLIC_SOURCE -> Triple(
            Color(0x2206B6D4),
            NeonCyan,
            Icons.Default.Public
        )
        VerificationStatus.ADMIN_UPDATED -> Triple(
            Color(0x228B5CF6),
            NeonPurple,
            Icons.Default.Security
        )
        VerificationStatus.AI_SUGGESTION -> Triple(
            Color(0x22F59E0B),
            NeonAmber,
            Icons.Default.AutoAwesome
        )
    }

    val label = if (currentLanguage == ContentLanguage.HINDI) status.labelHi else status.labelEn

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.5f)),
        modifier = modifier.testTag("status_badge_${status.name}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun GlowCard(
    modifier: Modifier = Modifier,
    borderColor: Color = DarkNavyBorder,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    badgeText: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 20.dp)
                        .background(
                            Brush.verticalGradient(listOf(NeonPurple, NeonPink)),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            if (badgeText != null) {
                Surface(
                    color = Color(0x338B5CF6),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = NeonPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun DisclaimerCard(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0x1F1E293B),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF334155)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Disclaimer info",
                tint = NeonCyan,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
fun LanguageToggleBar(
    currentLanguage: ContentLanguage,
    onLanguageSelected: (ContentLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier
    ) {
        Surface(
            color = DarkNavySurface,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, DarkNavyBorder)
        ) {
            Row(
                modifier = Modifier.padding(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LanguageChip(
                    text = "हिन्दी",
                    isSelected = currentLanguage == ContentLanguage.HINDI,
                    onClick = { onLanguageSelected(ContentLanguage.HINDI) }
                )
                Spacer(modifier = Modifier.width(2.dp))
                LanguageChip(
                    text = "Hinglish",
                    isSelected = currentLanguage == ContentLanguage.HINGLISH,
                    onClick = { onLanguageSelected(ContentLanguage.HINGLISH) }
                )
                Spacer(modifier = Modifier.width(2.dp))
                LanguageChip(
                    text = "English",
                    isSelected = currentLanguage == ContentLanguage.ENGLISH,
                    onClick = { onLanguageSelected(ContentLanguage.ENGLISH) }
                )
            }
        }
    }
}

@Composable
private fun LanguageChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) NeonPurple else Color.Transparent,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clickable { onClick() }
            .testTag("lang_chip_$text")
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else TextMuted,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}
