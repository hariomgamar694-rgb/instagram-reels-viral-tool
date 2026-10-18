package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ContentLanguage
import com.example.data.model.CreatorNiche
import com.example.data.model.TrendItem
import com.example.ui.components.DisclaimerCard
import com.example.ui.components.GlowCard
import com.example.ui.components.LanguageToggleBar
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.DarkNavyBackground
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
import com.example.ui.viewmodel.MainViewModel
import com.example.util.LanguageManager

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    onNavigateToAudio: () -> Unit,
    onNavigateToHashtags: () -> Unit,
    onNavigateToAiWriter: () -> Unit,
    onNavigateToCoach: () -> Unit,
    onNavigateToPostingTime: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToSavedVault: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLang by mainViewModel.currentLanguage.collectAsStateWithLifecycle()
    val selectedNiche by mainViewModel.selectedNiche.collectAsStateWithLifecycle()
    val trends by mainViewModel.trends.collectAsStateWithLifecycle()
    val savedCount by mainViewModel.totalSavedCount.collectAsStateWithLifecycle()
    val userProfile by mainViewModel.userProfile.collectAsStateWithLifecycle()

    val isHindi = currentLang == ContentLanguage.HINDI

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Header
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "HR Viral AI",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color(0x3310B981),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "LIVE",
                                    color = NeonGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = LanguageManager.getTitle("tagline", currentLang),
                            fontSize = 13.sp,
                            color = NeonPurple,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    LanguageToggleBar(
                        currentLanguage = currentLang,
                        onLanguageSelected = { mainViewModel.setLanguage(it) }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Welcome Card with quick saved stats
                Surface(
                    color = DarkNavySurface,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkNavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(
                                        Brush.linearGradient(listOf(NeonPurple, NeonPink)),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Creator AI",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isHindi) "नमस्ते, ${userProfile.name}!" else "Welcome back, ${userProfile.name}!",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${userProfile.instagramHandle} • ${selectedNiche.displayNameEn}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Surface(
                            color = Color(0x228B5CF6),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.clickable { onNavigateToSavedVault() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = "Saved Items",
                                    tint = NeonPurple,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$savedCount Saved",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Niche Carousel Selector
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isHindi) "अपना नीश चुनें (Niche)" else "Select Your Creator Niche",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(CreatorNiche.values()) { niche ->
                        val isSelected = niche == selectedNiche
                        Surface(
                            color = if (isSelected) NeonPurple else DarkNavySurface,
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) NeonPurple else DarkNavyBorder
                            ),
                            modifier = Modifier
                                .clickable { mainViewModel.setNiche(niche) }
                                .testTag("niche_chip_${niche.name}")
                        ) {
                            Text(
                                text = if (isHindi) niche.displayNameHi else niche.displayNameEn,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                            )
                        }
                    }
                }
            }
        }

        // Quick Feature Grid (All working modules)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                SectionHeader(
                    title = if (isHindi) "क्रिएटर टूल्स" else "Creator Power Tools",
                    subtitle = if (isHindi) "रील बनाने के सभी ज़रूरी टूल्स" else "Everything you need to craft high-retention Reels"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ToolTile(
                            title = LanguageManager.getTitle("nav_audio", currentLang),
                            subtitle = if (isHindi) "ट्रेंडिंग ऑडियो" else "Trending Audio",
                            icon = Icons.Default.MusicNote,
                            accentColor = NeonCyan,
                            testTag = "tile_trending_audio",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToAudio
                        )
                        ToolTile(
                            title = LanguageManager.getTitle("nav_hashtags", currentLang),
                            subtitle = if (isHindi) "स्मार्ट हैशटैग" else "Smart Hashtags",
                            icon = Icons.Default.Tag,
                            accentColor = NeonPink,
                            testTag = "tile_hashtags",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToHashtags
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ToolTile(
                            title = LanguageManager.getTitle("nav_ai_writer", currentLang),
                            subtitle = if (isHindi) "हुक और स्क्रिप्ट" else "Viral Hooks & Script",
                            icon = Icons.Default.AutoAwesome,
                            accentColor = NeonPurple,
                            testTag = "tile_ai_writer",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToAiWriter
                        )
                        ToolTile(
                            title = LanguageManager.getTitle("nav_coach", currentLang),
                            subtitle = if (isHindi) "ग्रोथ कोच ऑडिट" else "Retention Coach",
                            icon = Icons.Default.TrendingUp,
                            accentColor = NeonGreen,
                            testTag = "tile_growth_coach",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToCoach
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ToolTile(
                            title = LanguageManager.getTitle("nav_timing", currentLang),
                            subtitle = if (isHindi) "बेस्ट पोस्टिंग टाइम" else "Best Posting Time",
                            icon = Icons.Default.Schedule,
                            accentColor = NeonAmber,
                            testTag = "tile_posting_time",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToPostingTime
                        )
                        ToolTile(
                            title = LanguageManager.getTitle("nav_analytics", currentLang),
                            subtitle = if (isHindi) "परफॉर्मेंस ट्रैकर" else "Growth Tracker",
                            icon = Icons.Default.Analytics,
                            accentColor = NeonCyan,
                            testTag = "tile_analytics",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToAnalytics
                        )
                    }
                }
            }
        }

        // Today's Trend Updates
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                SectionHeader(
                    title = LanguageManager.getTitle("section_trend_updates", currentLang),
                    subtitle = if (isHindi) "आज क्या चल रहा है और कैसे बनाएं" else "Fresh algorithm-friendly patterns and formats",
                    badgeText = "${trends.size} Updates"
                )

                Spacer(modifier = Modifier.height(12.dp))

                trends.forEach { trend ->
                    TrendCard(
                        trend = trend,
                        currentLang = currentLang,
                        onUseHookClick = onNavigateToAiWriter
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        // Daily Viral Rule of Thumb
        item {
            GlowCard(borderColor = NeonPurple.copy(alpha = 0.4f)) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Daily tip",
                            tint = NeonAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "आज का वायरल नियम" else "Rule of Retention",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isHindi)
                            "इंस्टाग्राम एल्गोरिथम 'शेयर' (Shares) और 'सेव' (Saves) को लाइक्स से 5x ज्यादा महत्व देता है। अपनी हर रील के अंत में एक ऐसी वजह दें जिससे दर्शक दोस्त को भेजें या दोबारा देखने के लिए सेव करें।"
                        else
                            "Instagram prioritizes Shares and Saves over vanity likes. Craft content that triggers 'I need to send this to a friend' or 'I need this checklist later'.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Honest Disclaimer
        item {
            DisclaimerCard(
                text = if (isHindi)
                    "पारदर्शिता सूचना: यह ऐप वास्तविक एल्गोरिदम संकेतों के आधार पर सिफारिशें देता है। कोई भी हैशटैग, ऑडियो या समय 100% वायरल होने की गारंटी नहीं देता। निरंतरता और आकर्षक कंटेंट ही ऑर्गेनिक ग्रोथ की कुंजी है।"
                else
                    "Transparency Notice: HR Viral AI provides recommendations based on algorithmic retention patterns. No tool guarantees virality. True growth comes from consistency, testing hooks, and audience value."
            )
        }
    }
}

@Composable
private fun ToolTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = DarkNavyCard,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkNavyBorder),
        modifier = modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(accentColor.copy(alpha = 0.15f), shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TrendCard(
    trend: TrendItem,
    currentLang: ContentLanguage,
    onUseHookClick: () -> Unit
) {
    GlowCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = trend.verificationStatus, currentLanguage = currentLang)
                Text(
                    text = "${trend.popularityScore}% Momentum",
                    color = NeonGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = trend.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = trend.description,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 18.sp
            )

            if (trend.hookIdea.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0x338B5CF6),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "💡 Hook Idea / हुक का विचार:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonPurple
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "\"${trend.hookIdea}\"",
                            fontSize = 13.sp,
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = trend.hashtags.take(3).joinToString(" "),
                    color = NeonCyan,
                    fontSize = 11.sp
                )

                Surface(
                    color = NeonPurple,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { onUseHookClick() }
                ) {
                    Text(
                        text = if (currentLang == ContentLanguage.HINDI) "स्क्रिप्ट बनाएं" else "Create Script",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}
