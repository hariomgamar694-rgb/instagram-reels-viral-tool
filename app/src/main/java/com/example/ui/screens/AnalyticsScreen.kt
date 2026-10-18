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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.local.AnalyticsEntity
import com.example.data.model.ContentLanguage
import com.example.ui.components.DisclaimerCard
import com.example.ui.components.GlowCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.DarkNavyBackground
import com.example.ui.theme.DarkNavyBorder
import com.example.ui.theme.DarkNavyCard
import com.example.ui.theme.DarkNavySurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AnalyticsViewModel
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AnalyticsScreen(
    analyticsViewModel: AnalyticsViewModel,
    mainViewModel: MainViewModel,
    onNavigateToCoach: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLang by mainViewModel.currentLanguage.collectAsStateWithLifecycle()
    val isHindi = currentLang == ContentLanguage.HINDI

    val reels by analyticsViewModel.allReels.collectAsStateWithLifecycle()
    val avgViews by analyticsViewModel.averageViews.collectAsStateWithLifecycle()
    val avgWatch by analyticsViewModel.averageWatchTime.collectAsStateWithLifecycle()

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
                title = if (isHindi) "ग्रोथ एनालिटिक्स ट्रैकर" else "Growth Analytics Tracker",
                subtitle = if (isHindi) "आपके द्वारा सेव किए गए रील परफॉर्मेंस का इतिहास" else "Logged reel performance and retention metrics"
            )
        }

        // Summary KPI Banner
        item {
            GlowCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KpiStatItem(
                        label = if (isHindi) "कुल रील्स" else "Tracked Reels",
                        value = "${reels.size}",
                        accentColor = NeonPurple
                    )
                    KpiStatItem(
                        label = if (isHindi) "औसत व्यूज" else "Avg Views",
                        value = if (avgViews > 0) String.format("%.0f", avgViews) else "--",
                        accentColor = NeonCyan
                    )
                    KpiStatItem(
                        label = if (isHindi) "औसत वॉच %" else "Avg Watch %",
                        value = if (avgWatch > 0) "${String.format("%.1f", avgWatch)}%" else "--",
                        accentColor = NeonGreen
                    )
                }
            }
        }

        if (reels.isEmpty()) {
            item {
                GlowCard {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "No data",
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isHindi) "अभी तक कोई रील सेव नहीं की गई" else "No Reels Logged Yet",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isHindi) "ग्रोथ कोच में जाकर अपने रील मैट्रिक्स का विश्लेषण करें और उसे यहां सेव करें।" else "Analyze your Reel metrics in the Growth Coach and save them here to track your consistency.",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onNavigateToCoach,
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                        ) {
                            Text(if (isHindi) "ग्रोथ कोच खोलें" else "Open Growth Coach")
                        }
                    }
                }
            }
        } else {
            items(reels) { reel ->
                ReelHistoryCard(
                    reel = reel,
                    isHindi = isHindi,
                    onDelete = { analyticsViewModel.deleteEntry(reel.id) }
                )
            }
        }

        // Disclaimer
        item {
            DisclaimerCard(
                text = if (isHindi)
                    "नोट: यह डेटा स्थानीय रूप से आपके डिवाइस पर सुरक्षित रूप से सहेजा गया है। इंस्टाग्राम इनसाइट्स एपीआई कनेक्ट होने पर स्वचालित सिंक सक्षम होगा।"
                else
                    "Privacy Note: Performance analytics are saved locally on your device. Once official Instagram Insights API authorization is supported, automated sync will be enabled."
            )
        }
    }
}

@Composable
private fun KpiStatItem(
    label: String,
    value: String,
    accentColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = accentColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}

@Composable
private fun ReelHistoryCard(
    reel: AnalyticsEntity,
    isHindi: Boolean,
    onDelete: () -> Unit
) {
    GlowCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reel.reelTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0x3310B981),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${reel.coachScore}/100",
                            color = NeonGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Metrics row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "👁️ ${reel.views} Views",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    text = "⏱️ ${reel.watchTimePercentage}% Watch",
                    fontSize = 12.sp,
                    color = NeonCyan
                )
                Text(
                    text = "📤 ${reel.shares} Shares",
                    fontSize = 12.sp,
                    color = NeonPink
                )
                Text(
                    text = "💾 ${reel.saves} Saves",
                    fontSize = 12.sp,
                    color = NeonPurple
                )
            }

            if (reel.adviceText.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = DarkNavySurface,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "💡 ${reel.adviceText}",
                        fontSize = 12.sp,
                        color = TextPrimary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
