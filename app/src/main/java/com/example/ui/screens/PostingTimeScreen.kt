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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ContentLanguage
import com.example.data.model.CreatorNiche
import com.example.data.repository.PostingTimeWindow
import com.example.ui.components.DisclaimerCard
import com.example.ui.components.GlowCard
import com.example.ui.components.SectionHeader
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
import com.example.ui.viewmodel.PostingTimeViewModel

@Composable
fun PostingTimeScreen(
    postingViewModel: PostingTimeViewModel,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentLang by mainViewModel.currentLanguage.collectAsStateWithLifecycle()
    val isHindi = currentLang == ContentLanguage.HINDI

    val selectedNiche by postingViewModel.selectedNiche.collectAsStateWithLifecycle()
    val recommendation by postingViewModel.recommendation.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            SectionHeader(
                title = if (isHindi) "पोस्ट करने का सबसे सही समय" else "Best Posting Time",
                subtitle = if (isHindi) "ऑडियंस एक्टिविटी और नीश के अनुसार समय" else "Estimated peak audience windows across India & Global IST"
            )
        }

        // Niche Carousel
        item {
            Column {
                Text(
                    text = if (isHindi) "क्रिएटर नीश बदलें:" else "Filter by Niche:",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(CreatorNiche.values()) { niche ->
                        val isSelected = niche == selectedNiche
                        Surface(
                            color = if (isSelected) NeonPurple else DarkNavySurface,
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) NeonPurple else DarkNavyBorder
                            ),
                            modifier = Modifier
                                .clickable { postingViewModel.setNiche(niche) }
                                .testTag("posting_niche_${niche.name}")
                        ) {
                            Text(
                                text = if (isHindi) niche.displayNameHi else niche.displayNameEn,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Top Golden Windows for this Niche
        item {
            GlowCard(borderColor = NeonAmber.copy(alpha = 0.5f)) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "गोल्डन स्लॉट्स (भारतीय समय IST)" else "Peak Time Slots (IST)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Surface(
                            color = Color(0x33F59E0B),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Highest Reach",
                                color = NeonAmber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Slot 1: Evening Prime
                    TimeSlotRow(
                        icon = Icons.Default.Nightlight,
                        slotLabel = if (isHindi) "शाम का प्राइम स्लॉट (Evening Peak)" else "Evening Prime Slot",
                        timeRange = recommendation.bestSlotEveningIST,
                        iconTint = NeonPurple
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Slot 2: Lunch Break
                    TimeSlotRow(
                        icon = Icons.Default.WbSunny,
                        slotLabel = if (isHindi) "दोपहर का स्लॉट (Lunch Break)" else "Lunch Break Slot",
                        timeRange = recommendation.bestSlotLunchIST,
                        iconTint = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Slot 3: Morning
                    TimeSlotRow(
                        icon = Icons.Default.Schedule,
                        slotLabel = if (isHindi) "सुबह का स्लॉट (Morning Routine)" else "Morning Early Birds",
                        timeRange = recommendation.bestSlotMorningIST,
                        iconTint = NeonGreen
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "💡 Insight: ${recommendation.generalAudienceInsights}",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Daily Detailed Schedule
        item {
            Text(
                text = if (isHindi) "दिन-प्रतिदिन का शेड्यूल" else "Day-by-Day Activity Schedule",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        items(recommendation.dailySchedule) { day ->
            DailyWindowCard(window = day, isHindi = isHindi)
        }

        // Connection Status Note & Disclaimer
        item {
            DisclaimerCard(
                text = recommendation.connectionStatusNote
            )
        }
    }
}

@Composable
private fun TimeSlotRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    slotLabel: String,
    timeRange: String,
    iconTint: Color
) {
    Surface(
        color = DarkNavySurface,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = slotLabel,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = slotLabel,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = timeRange,
                color = iconTint,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DailyWindowCard(
    window: PostingTimeWindow,
    isHindi: Boolean
) {
    GlowCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = window.dayOfWeek,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Surface(
                    color = if (window.audienceActiveDensity.contains("Highest")) Color(0x3310B981) else Color(0x338B5CF6),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = window.audienceActiveDensity,
                        color = if (window.audienceActiveDensity.contains("Highest")) NeonGreen else NeonPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🎯 Best Window:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = window.peakTimeWindowIST,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Secondary:",
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = window.secondaryWindowIST,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = window.whyItWorks,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}
