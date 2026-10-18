package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ContentLanguage
import com.example.data.model.CreatorNiche
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
import com.example.ui.viewmodel.GrowthCoachViewModel
import com.example.ui.viewmodel.MainViewModel

@Composable
fun GrowthCoachScreen(
    coachViewModel: GrowthCoachViewModel,
    mainViewModel: MainViewModel,
    onNavigateToTracker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLang by mainViewModel.currentLanguage.collectAsStateWithLifecycle()
    val isHindi = currentLang == ContentLanguage.HINDI

    val reelTitle by coachViewModel.reelTitle.collectAsStateWithLifecycle()
    val selectedNiche by coachViewModel.selectedNiche.collectAsStateWithLifecycle()
    val goal by coachViewModel.contentGoal.collectAsStateWithLifecycle()

    val viewsInput by coachViewModel.viewsInput.collectAsStateWithLifecycle()
    val watchTimeInput by coachViewModel.watchTimeInput.collectAsStateWithLifecycle()
    val likesInput by coachViewModel.likesInput.collectAsStateWithLifecycle()
    val commentsInput by coachViewModel.commentsInput.collectAsStateWithLifecycle()
    val sharesInput by coachViewModel.sharesInput.collectAsStateWithLifecycle()
    val savesInput by coachViewModel.savesInput.collectAsStateWithLifecycle()

    val auditResult by coachViewModel.auditResult.collectAsStateWithLifecycle()
    val isSaved by coachViewModel.isSavedToTracker.collectAsStateWithLifecycle()

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
                title = if (isHindi) "रील ग्रोथ कोच व ऑडिट" else "Reel Growth Coach & Audit",
                subtitle = if (isHindi) "अपने रील मैट्रिक्स दर्ज करें और वास्तविक एल्गोरिदम सुधार प्राप्त करें" else "Input your Reel metrics to diagnose retention & sharing bottlenecks"
            )
        }

        // Metrics Input Card
        item {
            GlowCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (isHindi) "रील जानकारी और एनालिटिक्स दर्ज करें:" else "Enter Your Reel Insights:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    // Title
                    OutlinedTextField(
                        value = reelTitle,
                        onValueChange = { coachViewModel.reelTitle.value = it },
                        label = { Text("Reel Title / Hook Summary", color = TextSecondary) },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkNavySurface,
                            unfocusedContainerColor = DarkNavySurface,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = DarkNavyBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("coach_reel_title_input")
                    )

                    // Niche selector
                    Text(
                        text = if (isHindi) "नीश (Niche):" else "Niche:",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(CreatorNiche.values()) { niche ->
                            val isSelected = niche == selectedNiche
                            Surface(
                                color = if (isSelected) NeonPurple else DarkNavySurface,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.clickable { coachViewModel.selectedNiche.value = niche }
                            ) {
                                Text(
                                    text = if (isHindi) niche.displayNameHi else niche.displayNameEn,
                                    color = if (isSelected) Color.White else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    // Views & Watch Time % (2 columns)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = viewsInput,
                            onValueChange = { coachViewModel.viewsInput.value = it },
                            label = { Text("Total Views", color = TextSecondary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkNavySurface,
                                unfocusedContainerColor = DarkNavySurface,
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = DarkNavyBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("coach_views_input")
                        )

                        OutlinedTextField(
                            value = watchTimeInput,
                            onValueChange = { coachViewModel.watchTimeInput.value = it },
                            label = { Text("Avg Watch %", color = TextSecondary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkNavySurface,
                                unfocusedContainerColor = DarkNavySurface,
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = DarkNavyBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("coach_watch_time_input")
                        )
                    }

                    // Shares & Saves (The two most critical signals!)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = sharesInput,
                            onValueChange = { coachViewModel.sharesInput.value = it },
                            label = { Text("Shares (शेयर)", color = TextSecondary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkNavySurface,
                                unfocusedContainerColor = DarkNavySurface,
                                focusedBorderColor = NeonPink,
                                unfocusedBorderColor = DarkNavyBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("coach_shares_input")
                        )

                        OutlinedTextField(
                            value = savesInput,
                            onValueChange = { coachViewModel.savesInput.value = it },
                            label = { Text("Saves (सेव)", color = TextSecondary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkNavySurface,
                                unfocusedContainerColor = DarkNavySurface,
                                focusedBorderColor = NeonPink,
                                unfocusedBorderColor = DarkNavyBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("coach_saves_input")
                        )
                    }

                    // Likes & Comments
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = likesInput,
                            onValueChange = { coachViewModel.likesInput.value = it },
                            label = { Text("Likes", color = TextSecondary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkNavySurface,
                                unfocusedContainerColor = DarkNavySurface,
                                focusedBorderColor = DarkNavyBorder,
                                unfocusedBorderColor = DarkNavyBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = commentsInput,
                            onValueChange = { coachViewModel.commentsInput.value = it },
                            label = { Text("Comments", color = TextSecondary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkNavySurface,
                                unfocusedContainerColor = DarkNavySurface,
                                focusedBorderColor = DarkNavyBorder,
                                unfocusedBorderColor = DarkNavyBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Analyze Button
                    Button(
                        onClick = { coachViewModel.runAudit() },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("coach_run_audit_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Audit",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "एल्गोरिदम ऑडिट चलाएं" else "Run Algorithmic Audit",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Audit Results Display
        auditResult?.let { result ->
            item {
                GlowCard(borderColor = if (result.overallHealthScore >= 70) NeonGreen else NeonAmber) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Score & Hook Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isHindi) "एल्गोरिदम हेल्थ स्कोर" else "Algorithm Health Score",
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "${result.overallHealthScore}",
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (result.overallHealthScore >= 70) NeonGreen else NeonAmber
                                    )
                                    Text(
                                        text = "/100",
                                        fontSize = 16.sp,
                                        color = TextMuted,
                                        modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                                    )
                                }
                            }

                            Surface(
                                color = Color(0x338B5CF6),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = result.hookRating,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = result.hookExplanation,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Benchmark Signal Metrics
                        Surface(
                            color = DarkNavySurface,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "📊 Key Recommendation Ratios:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = result.algorithmSignalsEvaluation,
                                    fontSize = 13.sp,
                                    color = TextPrimary,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Steps
                        Text(
                            text = if (isHindi) "सुधार के लिए प्राथमिकता कदम:" else "Prioritized Action Steps:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        result.prioritizedActionSteps.forEach { step ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Action",
                                    tint = NeonGreen,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = step,
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Save to Tracker button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    coachViewModel.saveToAnalyticsTracker {
                                        Toast.makeText(context, "Saved to Performance Tracker! 📈", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = if (isSaved) NeonGreen else NeonCyan),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = "Save",
                                    tint = DarkNavyBackground,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isSaved) "Saved to Tracker ✓" else "Save to Performance Tracker",
                                    color = DarkNavyBackground,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                text = "View History →",
                                color = NeonPurple,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onNavigateToTracker() }
                            )
                        }
                    }
                }
            }
        }

        // Disclaimer
        item {
            DisclaimerCard(
                text = if (isHindi)
                    "नोट: यह ग्रोथ कोच व्यावहारिक सलाह प्रदान करता है। कोई भी प्रणाली वायरल होने की गारंटी नहीं दे सकती। इंस्टाग्राम एल्गोरिदम व्यक्तिगत उपयोगकर्ता प्राथमिकताओं और निरंतर दर्शकों के जुड़ाव पर निर्भर करता है।"
                else
                    "Notice: HR Viral AI provides diagnostic suggestions based on audience retention patterns. No tool guarantees virality or follower counts."
            )
        }
    }
}
