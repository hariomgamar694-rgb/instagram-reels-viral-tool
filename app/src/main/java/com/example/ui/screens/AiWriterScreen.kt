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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CaptionStyle
import com.example.data.model.ContentLanguage
import com.example.data.model.ContentPlan
import com.example.data.model.CreatorNiche
import com.example.data.model.GeneratedCaption
import com.example.data.model.ReelScript
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
import com.example.ui.viewmodel.AiWriterViewModel
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.WriterTab
import com.example.util.ShareUtils

@Composable
fun AiWriterScreen(
    writerViewModel: AiWriterViewModel,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLang by mainViewModel.currentLanguage.collectAsStateWithLifecycle()
    val isHindi = currentLang == ContentLanguage.HINDI

    val activeTab by writerViewModel.activeTab.collectAsStateWithLifecycle()
    val topic by writerViewModel.topic.collectAsStateWithLifecycle()
    val selectedNiche by writerViewModel.selectedNiche.collectAsStateWithLifecycle()
    val selectedLang by writerViewModel.selectedLanguage.collectAsStateWithLifecycle()
    val selectedStyle by writerViewModel.selectedStyle.collectAsStateWithLifecycle()

    val caption by writerViewModel.generatedCaption.collectAsStateWithLifecycle()
    val script by writerViewModel.generatedScript.collectAsStateWithLifecycle()
    val calendar by writerViewModel.contentCalendar.collectAsStateWithLifecycle()
    val isLoading by writerViewModel.isLoading.collectAsStateWithLifecycle()
    val statusNotice by writerViewModel.statusNotice.collectAsStateWithLifecycle()

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
                title = if (isHindi) "एआई कैप्शन और स्क्रिप्ट राइटर" else "AI Caption & Script Writer",
                subtitle = if (isHindi) "3-सेकंड हुक, कैप्शन, 3-सीन स्क्रिप्ट और 7-दिन का कैलेंडर" else "First 3s hooks, bilingual captions, scene breakdowns & calendar"
            )
        }

        // Segmented Tab Control
        item {
            Surface(
                color = DarkNavySurface,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    TabButton(
                        title = if (isHindi) "कैप्शन और हुक" else "Captions & Hooks",
                        icon = Icons.Default.Notes,
                        isSelected = activeTab == WriterTab.CAPTIONS_AND_HOOKS,
                        modifier = Modifier.weight(1f),
                        onClick = { writerViewModel.setTab(WriterTab.CAPTIONS_AND_HOOKS) }
                    )
                    TabButton(
                        title = if (isHindi) "रील स्क्रिप्ट" else "Reel Script",
                        icon = Icons.Default.Movie,
                        isSelected = activeTab == WriterTab.REEL_SCRIPTS,
                        modifier = Modifier.weight(1f),
                        onClick = { writerViewModel.setTab(WriterTab.REEL_SCRIPTS) }
                    )
                    TabButton(
                        title = if (isHindi) "कंटेंट कैलेंडर" else "Calendar",
                        icon = Icons.Default.CalendarMonth,
                        isSelected = activeTab == WriterTab.CONTENT_CALENDAR,
                        modifier = Modifier.weight(1f),
                        onClick = { writerViewModel.setTab(WriterTab.CONTENT_CALENDAR) }
                    )
                }
            }
        }

        // Shared Inputs Card
        item {
            GlowCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Topic
                    Column {
                        Text(
                            text = if (isHindi) "विषय / टॉपिक:" else "Reel Concept / Topic:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = topic,
                            onValueChange = { writerViewModel.topic.value = it },
                            placeholder = { Text("Enter reel topic...", color = TextMuted) },
                            shape = RoundedCornerShape(12.dp),
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
                                .testTag("ai_writer_topic_input")
                        )
                    }

                    // Language Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "भाषा:" else "Language:",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            ContentLanguage.values().forEach { lang ->
                                val isSelected = lang == selectedLang
                                Surface(
                                    color = if (isSelected) NeonPurple else DarkNavySurface,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.clickable { writerViewModel.selectedLanguage.value = lang }
                                ) {
                                    Text(
                                        text = lang.nativeName,
                                        color = if (isSelected) Color.White else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Main Action Button
                    Button(
                        onClick = {
                            when (activeTab) {
                                WriterTab.CAPTIONS_AND_HOOKS -> writerViewModel.generateCaption()
                                WriterTab.REEL_SCRIPTS -> writerViewModel.generateScript()
                                WriterTab.CONTENT_CALENDAR -> writerViewModel.generateCalendar()
                            }
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("ai_writer_generate_btn")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Generate",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (activeTab) {
                                    WriterTab.CAPTIONS_AND_HOOKS -> if (isHindi) "कैप्शन और हुक जनरेट करें" else "Generate Caption & Hook"
                                    WriterTab.REEL_SCRIPTS -> if (isHindi) "फुल रील स्क्रिप्ट जनरेट करें" else "Generate 3-Scene Script"
                                    WriterTab.CONTENT_CALENDAR -> if (isHindi) "7-दिन का कैलेंडर तैयार करें" else "Generate 7-Day Plan"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Render Tab Specific Results
        when (activeTab) {
            WriterTab.CAPTIONS_AND_HOOKS -> {
                caption?.let { cap ->
                    item {
                        CaptionOutputCard(
                            caption = cap,
                            isHindi = isHindi,
                            onModify = { action -> writerViewModel.modifyCaption(action) },
                            onSave = {
                                writerViewModel.saveCaptionToVault {
                                    Toast.makeText(context, "Saved to Vault! 💾", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onCopy = {
                                val full = "🪝 HOOK:\n${cap.firstThreeSecondsHook}\n\n📝 CAPTION:\n${cap.captionText}\n\n🎯 CTA:\n${cap.callToAction}\n\n🏷️ TAGS:\n${cap.hashtags.joinToString(" ")}"
                                ShareUtils.copyToClipboard(context, full, "Viral Caption")
                            }
                        )
                    }
                }
            }

            WriterTab.REEL_SCRIPTS -> {
                script?.let { scr ->
                    item {
                        ScriptOutputCard(
                            script = scr,
                            isHindi = isHindi,
                            onSave = {
                                writerViewModel.saveScriptToVault {
                                    Toast.makeText(context, "Script Saved to Vault! 🎬", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onCopy = {
                                val full = buildString {
                                    append("🎬 ${scr.title}\n")
                                    append("⏱️ ${scr.estimatedDurationSeconds}s | 🎵 ${scr.audioRecommendation}\n\n")
                                    scr.scenes.forEach { s ->
                                        append("[Scene ${s.sceneNumber}: ${s.timeCode}]\nVisual: ${s.visualAction}\nText: ${s.onScreenText}\nVO: ${s.voiceoverOrDialogue}\n\n")
                                    }
                                    append("CTA: ${scr.ctaClosing}")
                                }
                                ShareUtils.copyToClipboard(context, full, "Reel Script")
                            }
                        )
                    }
                }
            }

            WriterTab.CONTENT_CALENDAR -> {
                items(calendar) { plan ->
                    CalendarDayCard(plan = plan, isHindi = isHindi)
                }
            }
        }

        // Disclaimer
        item {
            DisclaimerCard(
                text = if (isHindi)
                    "नोट: एआई द्वारा तैयार किए गए हुक और कैप्शन उच्च रिटेंशन फॉर्मूलों पर आधारित हैं। अपनी व्यक्तिगत आवाज़ और वास्तविक अनुभव को जोड़कर इसे और अधिक प्रामाणिक बनाएं।"
                else
                    "Tip: High retention begins with high authenticity. Use these AI hooks as a solid foundation, and tailor them to your unique personal voice."
            )
        }
    }
}

@Composable
private fun TabButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) NeonPurple else Color.Transparent,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) Color.White else TextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                color = if (isSelected) Color.White else TextSecondary,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun CaptionOutputCard(
    caption: GeneratedCaption,
    isHindi: Boolean,
    onModify: (String) -> Unit,
    onSave: () -> Unit,
    onCopy: () -> Unit
) {
    GlowCard(borderColor = NeonPurple.copy(alpha = 0.5f)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0x33EC4899),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "🪝 3-Second Hook",
                        color = NeonPink,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onSave, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Save",
                            tint = NeonPurple,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Hook Text
            Text(
                text = "\"${caption.firstThreeSecondsHook}\"",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "📝 Caption Body:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = caption.captionText,
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = DarkNavySurface,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "🎯 Call To Action (CTA):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = caption.callToAction,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = caption.hashtags.joinToString(" "),
                fontSize = 12.sp,
                color = NeonPurple
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Modification Action Chips (Shorten, Expand, Funny, etc.)
            Text(
                text = if (isHindi) "कैप्शन को बदलें (AI Actions):" else "Refine with AI:",
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val actions = listOf(
                    Pair("Shorten ✂️", "SHORTEN"),
                    Pair("Expand 📖", "EXPAND"),
                    Pair("Rewrite 🔄", "REWRITE"),
                    Pair("Make Funny 😂", "MAKE_FUNNY"),
                    Pair("Inspirational 🔥", "MAKE_INSPIRATIONAL")
                )
                items(actions) { (label, actionKey) ->
                    Surface(
                        color = DarkNavySurface,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkNavyBorder),
                        modifier = Modifier.clickable { onModify(actionKey) }
                    ) {
                        Text(
                            text = label,
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScriptOutputCard(
    script: ReelScript,
    isHindi: Boolean,
    onSave: () -> Unit,
    onCopy: () -> Unit
) {
    GlowCard(borderColor = NeonCyan.copy(alpha = 0.5f)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0x3306B6D4),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "🎬 ${script.estimatedDurationSeconds}s Reel Script",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row {
                    IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onSave, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Save",
                            tint = NeonPurple,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = script.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "🎵 Recommended Audio: ${script.audioRecommendation}",
                fontSize = 12.sp,
                color = NeonPink
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Scenes breakdown
            script.scenes.forEach { scene ->
                Surface(
                    color = DarkNavySurface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkNavyBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "SCENE ${scene.sceneNumber}",
                                color = NeonPurple,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = scene.timeCode,
                                color = NeonGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "👀 Visual: ${scene.visualAction}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "💬 Text Overlay: ${scene.onScreenText}",
                            fontSize = 12.sp,
                            color = NeonCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "🎙️ Voiceover: \"${scene.voiceoverOrDialogue}\"",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                color = Color(0x2210B981),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🎯 Closing CTA: ${script.ctaClosing}",
                    fontSize = 12.sp,
                    color = NeonGreen,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
private fun CalendarDayCard(
    plan: ContentPlan,
    isHindi: Boolean
) {
    GlowCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0x338B5CF6),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = plan.dayTitle,
                        color = NeonPurple,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = plan.recommendedPostHourIST,
                    color = NeonGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Format: ${plan.format}",
                fontSize = 13.sp,
                color = NeonCyan,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = plan.topicDescription,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = DarkNavySurface,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "💡 Hook: \"${plan.hookIdea}\"",
                    fontSize = 12.sp,
                    color = TextPrimary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
