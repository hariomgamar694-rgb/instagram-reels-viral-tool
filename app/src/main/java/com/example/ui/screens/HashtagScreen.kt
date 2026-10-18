package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.CreatorNiche
import com.example.data.model.HashtagSuggestion
import com.example.ui.components.DisclaimerCard
import com.example.ui.components.GlowCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.DarkNavyBackground
import com.example.ui.theme.DarkNavyBorder
import com.example.ui.theme.DarkNavyCard
import com.example.ui.theme.DarkNavySurface
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.HashtagViewModel
import com.example.ui.viewmodel.MainViewModel
import com.example.util.ShareUtils

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HashtagScreen(
    hashtagViewModel: HashtagViewModel,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLang by mainViewModel.currentLanguage.collectAsStateWithLifecycle()
    val isHindi = currentLang == ContentLanguage.HINDI

    val topic by hashtagViewModel.topic.collectAsStateWithLifecycle()
    val selectedNiche by hashtagViewModel.selectedNiche.collectAsStateWithLifecycle()
    val selectedLang by hashtagViewModel.selectedLanguage.collectAsStateWithLifecycle()
    val audience by hashtagViewModel.targetAudience.collectAsStateWithLifecycle()
    val selectedStyle by hashtagViewModel.selectedStyle.collectAsStateWithLifecycle()

    val hashtagGroup by hashtagViewModel.hashtagGroup.collectAsStateWithLifecycle()
    val isLoading by hashtagViewModel.isLoading.collectAsStateWithLifecycle()
    val selectedTags by hashtagViewModel.selectedTags.collectAsStateWithLifecycle()
    val statusMessage by hashtagViewModel.statusMessage.collectAsStateWithLifecycle()

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
                title = if (isHindi) "स्मार्ट हैशटैग जनरेटर" else "Smart Hashtag Generator",
                subtitle = if (isHindi) "4 एल्गोरिदम-अनुकूल श्रेणियों में हैशटैग" else "Clustered into Broad, Niche, Community & Regional sets"
            )
        }

        // Input Form
        item {
            GlowCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Topic Input
                    Column {
                        Text(
                            text = if (isHindi) "रील का विषय / टॉपिक:" else "Reel Topic or Keyword:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = topic,
                            onValueChange = { hashtagViewModel.topic.value = it },
                            placeholder = { Text("e.g. Budget smartphone review / Morning routine", color = TextMuted) },
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
                                .testTag("hashtag_topic_input")
                        )
                    }

                    // Niche Selector
                    Column {
                        Text(
                            text = if (isHindi) "क्रिएटर नीश:" else "Creator Niche:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(CreatorNiche.values()) { niche ->
                                val isSelected = niche == selectedNiche
                                Surface(
                                    color = if (isSelected) NeonPurple else DarkNavySurface,
                                    shape = RoundedCornerShape(16.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) NeonPurple else DarkNavyBorder
                                    ),
                                    modifier = Modifier.clickable { hashtagViewModel.selectedNiche.value = niche }
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

                    // Language & Style Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) "भाषा:" else "Language:",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                ContentLanguage.values().forEach { lang ->
                                    val isSelected = lang == selectedLang
                                    Surface(
                                        color = if (isSelected) NeonCyan else DarkNavySurface,
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.clickable { hashtagViewModel.selectedLanguage.value = lang }
                                    ) {
                                        Text(
                                            text = lang.displayName,
                                            color = if (isSelected) DarkNavyBackground else TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Generate Button
                    Button(
                        onClick = { hashtagViewModel.generateHashtags() },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("generate_hashtags_button")
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
                                text = if (isHindi) "हैशटैग जनरेट करें" else "Generate Clustered Hashtags",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Action Toolbar: Copy All, Copy Selected, Save, Regenerate
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val text = hashtagViewModel.getAllTagsString()
                        if (text.isNotBlank()) {
                            ShareUtils.copyToClipboard(context, text, "HR Viral Hashtags")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkNavySurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f).testTag("copy_all_hashtags")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy All",
                        tint = NeonPurple,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isHindi) "सभी कॉपी करें" else "Copy All",
                        color = TextPrimary,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = {
                        val text = hashtagViewModel.getSelectedTagsString()
                        if (text.isNotBlank()) {
                            ShareUtils.copyToClipboard(context, text, "Selected Hashtags")
                        } else {
                            Toast.makeText(context, "Please tap on tags to select them first!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkNavySurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f).testTag("copy_selected_hashtags")
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Copy Selected",
                        tint = NeonCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${if (isHindi) "चयनित" else "Selected"} (${selectedTags.size})",
                        color = TextPrimary,
                        fontSize = 12.sp
                    )
                }

                IconButton(
                    onClick = {
                        hashtagViewModel.saveToVault {
                            Toast.makeText(context, "Saved to Vault! 💾", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .background(Color(0x338B5CF6), shape = RoundedCornerShape(10.dp))
                        .testTag("save_hashtags_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Save",
                        tint = NeonPurple
                    )
                }

                IconButton(
                    onClick = { hashtagViewModel.generateHashtags() },
                    modifier = Modifier.background(DarkNavySurface, shape = RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Regenerate",
                        tint = NeonPink
                    )
                }
            }
        }

        // Hashtag Clusters
        hashtagGroup?.let { group ->
            item {
                Text(
                    text = "Tip: Tap individual tags to select them for customized copying.",
                    color = TextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            // Broad Topic Tags
            item {
                HashtagClusterCard(
                    categoryTitle = if (isHindi) "1. व्यापक विषय (Broad Topic)" else "1. Broad Topic Tags",
                    categorySubtitle = "High discovery reach across Explore and general audience",
                    tags = group.broadTopicTags,
                    selectedTags = selectedTags,
                    onTagClick = { hashtagViewModel.toggleTagSelection(it) }
                )
            }

            // Specific Niche Tags
            item {
                HashtagClusterCard(
                    categoryTitle = if (isHindi) "2. विशिष्ट नीश (Specific Niche)" else "2. Specific Niche Tags",
                    categorySubtitle = "Pinpoints target audience searching for this exact topic",
                    tags = group.specificNicheTags,
                    selectedTags = selectedTags,
                    onTagClick = { hashtagViewModel.toggleTagSelection(it) }
                )
            }

            // Community Tags
            item {
                HashtagClusterCard(
                    categoryTitle = if (isHindi) "3. क्रिएटर कम्युनिटी (Community)" else "3. Creator Community Tags",
                    categorySubtitle = "Connects with fellow creators and engaged niches",
                    tags = group.communityTags,
                    selectedTags = selectedTags,
                    onTagClick = { hashtagViewModel.toggleTagSelection(it) }
                )
            }

            // Regional / Language Tags
            item {
                HashtagClusterCard(
                    categoryTitle = if (isHindi) "4. भाषा और क्षेत्र (Language / Region)" else "4. Language & Regional Tags",
                    categorySubtitle = "Signals geographical distribution to Indian audience",
                    tags = group.regionalLanguageTags,
                    selectedTags = selectedTags,
                    onTagClick = { hashtagViewModel.toggleTagSelection(it) }
                )
            }
        }

        // Disclaimer
        item {
            DisclaimerCard(
                text = if (isHindi)
                    "महत्वपूर्ण: उत्पन्न किए गए हैशटैग केवल एल्गोरिदम-अनुकूल सुझाव हैं, न कि सत्यापित लोकप्रियता रैंकिंग या वायरल टिकट। अत्यधिक (30+) हैशटैग से बचें; 5 से 15 प्रासंगिक टैग सबसे बेहतर परिणाम देते हैं।"
                else
                    "Important: Generated hashtags are algorithmic suggestions, not verified popularity rankings or guaranteed viral tickets. Quality and viewer retention dictate reach; keep tags between 5-15 highly relevant entries."
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HashtagClusterCard(
    categoryTitle: String,
    categorySubtitle: String,
    tags: List<HashtagSuggestion>,
    selectedTags: Set<String>,
    onTagClick: (String) -> Unit
) {
    GlowCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = categoryTitle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = categorySubtitle,
                fontSize = 12.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                tags.forEach { item ->
                    val isSelected = selectedTags.contains(item.tag)
                    Surface(
                        color = if (isSelected) NeonPurple else DarkNavySurface,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) NeonPurple else DarkNavyBorder
                        ),
                        modifier = Modifier
                            .clickable { onTagClick(item.tag) }
                            .testTag("tag_item_${item.tag}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = item.tag,
                                color = if (isSelected) Color.White else NeonCyan,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            if (isSelected) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
