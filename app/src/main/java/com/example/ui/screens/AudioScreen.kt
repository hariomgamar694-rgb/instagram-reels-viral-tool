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
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
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
import com.example.data.model.AudioItem
import com.example.data.model.ContentLanguage
import com.example.ui.components.DisclaimerCard
import com.example.ui.components.GlowCard
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
import com.example.ui.viewmodel.AudioUiState
import com.example.ui.viewmodel.AudioViewModel
import com.example.ui.viewmodel.MainViewModel
import com.example.util.ShareUtils

@Composable
fun AudioScreen(
    audioViewModel: AudioViewModel,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLang by mainViewModel.currentLanguage.collectAsStateWithLifecycle()
    val isHindi = currentLang == ContentLanguage.HINDI

    val uiState by audioViewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by audioViewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by audioViewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedLanguage by audioViewModel.selectedLanguage.collectAsStateWithLifecycle()

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
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader(
                        title = if (isHindi) "ट्रेंडिंग ऑडियो" else "Trending Audio",
                        subtitle = if (isHindi) "रील्स के लिए वेरिफाइड वायरल ऑडियो" else "Curated viral tracks and trending beats"
                    )
                    IconButton(
                        onClick = { audioViewModel.loadAudios() },
                        modifier = Modifier.testTag("refresh_audio_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = NeonPurple
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { audioViewModel.onSearchQueryChanged(it) },
                    placeholder = {
                        Text(
                            text = if (isHindi) "गीत, गायक या श्रेणी खोजें..." else "Search by song title, artist, or vibe...",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = NeonCyan
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { audioViewModel.onSearchQueryChanged("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = TextSecondary
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkNavySurface,
                        unfocusedContainerColor = DarkNavySurface,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = DarkNavyBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("audio_search_field")
                )
            }
        }

        // Language Filters
        item {
            Column {
                Text(
                    text = if (isHindi) "भाषा फिल्टर" else "Language Filter",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val allLangs = listOf(
                        Pair<String, ContentLanguage?>("All / सभी", null),
                        Pair("Hindi", ContentLanguage.HINDI),
                        Pair("English", ContentLanguage.ENGLISH)
                    )
                    allLangs.forEach { (label, lang) ->
                        val isSelected = selectedLanguage == lang
                        Surface(
                            color = if (isSelected) NeonPurple else DarkNavySurface,
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) NeonPurple else DarkNavyBorder
                            ),
                            modifier = Modifier.clickable { audioViewModel.setLanguageFilter(lang) }
                        ) {
                            Text(
                                text = label,
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

        // Category Filters
        item {
            Column {
                Text(
                    text = if (isHindi) "श्रेणी फिल्टर (Category)" else "Vibe & Category",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(audioViewModel.availableCategories) { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            color = if (isSelected) NeonCyan else DarkNavySurface,
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) NeonCyan else DarkNavyBorder
                            ),
                            modifier = Modifier.clickable { audioViewModel.setCategoryFilter(cat) }
                        ) {
                            Text(
                                text = cat,
                                color = if (isSelected) DarkNavyBackground else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Direct Instagram Audio Discovery Link
        item {
            Surface(
                color = Color(0x228B5CF6),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        ShareUtils.openUrl(context, "https://www.instagram.com/reels/audio/")
                    }
                    .testTag("open_instagram_audio_hub")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Instagram Audio Hub",
                            tint = NeonPink,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isHindi) "इंस्टाग्राम रील्स ऑडियो डिस्कवरी खोलें" else "Explore Live Instagram Audio Hub",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isHindi) "सीधे इंस्टाग्राम ऐप/वेब में ट्रेंडिंग म्यूज़िक देखें" else "Check official live Instagram audio trending charts directly",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "Open",
                        tint = NeonPurple,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Audio Items Content State
        when (val state = uiState) {
            is AudioUiState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NeonPurple)
                    }
                }
            }

            is AudioUiState.Empty -> {
                item {
                    GlowCard {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = "No audio",
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (isHindi) "कोई ऑडियो नहीं मिला" else "No matching audio found",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isHindi) "कृपया सर्च कीवर्ड बदलें या ऑल सेलेक्ट करें।" else "Try clearing your search query or selecting 'All' categories.",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    audioViewModel.onSearchQueryChanged("")
                                    audioViewModel.setCategoryFilter("All")
                                    audioViewModel.setLanguageFilter(null)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                            ) {
                                Text(if (isHindi) "फिल्टर रीसेट करें" else "Reset Filters")
                            }
                        }
                    }
                }
            }

            is AudioUiState.Error -> {
                item {
                    GlowCard {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Error: ${state.message}",
                                color = NeonPink,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { audioViewModel.loadAudios() },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                            ) {
                                Text(if (isHindi) "पुनः प्रयास करें" else "Retry")
                            }
                        }
                    }
                }
            }

            is AudioUiState.Success -> {
                items(state.audios) { audio ->
                    AudioCardItem(
                        audio = audio,
                        currentLang = currentLang,
                        onOpenInstagram = { ShareUtils.openUrl(context, audio.originalReelsUrl) },
                        onSaveToVault = {
                            audioViewModel.saveAudioToVault(audio) {
                                Toast.makeText(context, "Saved to Vault! 🎵", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }

        // Disclaimer
        item {
            DisclaimerCard(
                text = if (isHindi)
                    "नोट: ऑडियो ट्रेंड्स सार्वजनिक रूप से उपलब्ध लोकप्रिय रील्स गानों और क्रिएटर कम्युनिटी रिपोर्ट्स पर आधारित हैं। हम निजी डेटा स्क्रैप नहीं करते हैं और न ही किसी गाने के वायरल होने की झूठी गारंटी देते हैं।"
                else
                    "Disclaimer: Audio trends are curated from public viral charts and community creator usage. HR Viral AI does not scrape private user data or fabricate usage stats. No audio track guarantees virality."
            )
        }
    }
}

@Composable
private fun AudioCardItem(
    audio: AudioItem,
    currentLang: ContentLanguage,
    onOpenInstagram: () -> Unit,
    onSaveToVault: () -> Unit
) {
    GlowCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = audio.verificationStatus, currentLanguage = currentLang)
                Surface(
                    color = Color(0x3310B981),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "#${audio.momentumRank} Trend",
                        color = NeonGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0x338B5CF6), shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Audio track",
                        tint = NeonPurple,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = audio.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = audio.artist,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Vibe & Recommended Niches
            Surface(
                color = DarkNavySurface,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Vibe:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = audio.vibeTag,
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Best for: ${audio.recommendedNiches.joinToString(", ")}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0x338B5CF6),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { onSaveToVault() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Save audio",
                            tint = NeonPurple,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (currentLang == ContentLanguage.HINDI) "सेव करें" else "Save",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Button(
                    onClick = onOpenInstagram,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "Instagram link",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (currentLang == ContentLanguage.HINDI) "इंस्टा पर ऑडियो देखें" else "Check Audio on Insta",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
