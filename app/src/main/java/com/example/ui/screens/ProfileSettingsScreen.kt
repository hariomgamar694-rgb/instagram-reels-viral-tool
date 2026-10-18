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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ContentLanguage
import com.example.data.model.CreatorNiche
import com.example.data.remote.GeminiClient
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

@Composable
fun ProfileSettingsScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLang by mainViewModel.currentLanguage.collectAsStateWithLifecycle()
    val isHindi = currentLang == ContentLanguage.HINDI

    val userProfile by mainViewModel.userProfile.collectAsStateWithLifecycle()

    var nameInput by remember(userProfile.name) { mutableStateOf(userProfile.name) }
    var handleInput by remember(userProfile.instagramHandle) { mutableStateOf(userProfile.instagramHandle) }
    var selectedNiche by remember(userProfile.primaryNiche) { mutableStateOf(userProfile.primaryNiche) }

    val isGeminiKeySet = GeminiClient.isApiKeyConfigured()

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
                title = if (isHindi) "प्रोफाइल और सेटिंग्स" else "Profile & Settings",
                subtitle = if (isHindi) "क्रिएटर प्रोफाइल और एपीआई कनेक्शन स्थिति" else "Manage your creator persona, language and API status"
            )
        }

        // Creator Persona Card
        item {
            GlowCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Brush.linearGradient(listOf(NeonPurple, NeonPink)),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = nameInput,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = handleInput,
                                fontSize = 13.sp,
                                color = NeonPurple
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Creator / Channel Name", color = TextSecondary) },
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
                            .testTag("profile_name_input")
                    )

                    OutlinedTextField(
                        value = handleInput,
                        onValueChange = { handleInput = it },
                        label = { Text("Instagram Handle (e.g. @username)", color = TextSecondary) },
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
                            .testTag("profile_handle_input")
                    )

                    Text(
                        text = if (isHindi) "प्राथमिक नीश (Primary Niche):" else "Primary Creator Niche:",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(CreatorNiche.values()) { niche ->
                            val isSelected = niche == selectedNiche
                            Surface(
                                color = if (isSelected) NeonPurple else DarkNavySurface,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.clickable { selectedNiche = niche }
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

                    Button(
                        onClick = {
                            mainViewModel.updateProfile(nameInput, handleInput, selectedNiche)
                            Toast.makeText(context, "Profile updated! ✨", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isHindi) "प्रोफाइल सेव करें" else "Save Profile")
                    }
                }
            }
        }

        // App Language Preference
        item {
            GlowCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (isHindi) "डिफ़ॉल्ट भाषा प्राथमिकता (Language)" else "App Language Preference",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ContentLanguage.values().forEach { lang ->
                            val isSelected = lang == currentLang
                            Surface(
                                color = if (isSelected) NeonPurple else DarkNavySurface,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) NeonPurple else DarkNavyBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { mainViewModel.setLanguage(lang) }
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(
                                        text = lang.nativeName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else TextPrimary
                                    )
                                    Text(
                                        text = lang.displayName,
                                        fontSize = 11.sp,
                                        color = if (isSelected) Color.White.copy(alpha = 0.8f) else TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Connections & Status Dashboard (Honest Integrations)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isHindi) "सिस्टम व एपीआई कनेक्शन स्थिति" else "Integrations & API Status",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // 1. Gemini AI Status
                IntegrationStatusCard(
                    serviceName = "Gemini AI Engine (gemini-3.5-flash)",
                    statusText = if (isGeminiKeySet) "Active & Connected (Secrets Key)" else "Offline Algorithmic Fallback Active",
                    statusDetail = if (isGeminiKeySet)
                        "Powered by Google Gemini 3.5 Flash via secure AI Studio BuildConfig injection."
                    else
                        "Configure GEMINI_API_KEY in AI Studio Secrets panel for live generative models.",
                    isConnected = isGeminiKeySet,
                    icon = Icons.Default.AutoAwesome
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 2. Local Database & Cloud Sync
                IntegrationStatusCard(
                    serviceName = "Device Vault (Room Local DB)",
                    statusText = "Active & Persisted",
                    statusDetail = "All saved captions, scripts, hashtags and analytics are stored offline on this device.",
                    isConnected = true,
                    icon = Icons.Default.Cloud
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 3. Instagram Insights API
                IntegrationStatusCard(
                    serviceName = "Instagram Insights API (Meta Graph)",
                    statusText = "Not Connected (General Benchmarks Active)",
                    statusDetail = "Requires Meta App Review & OAuth credentials. Currently using aggregated creator benchmarks.",
                    isConnected = false,
                    icon = Icons.Default.Link
                )
            }
        }

        // Brand Info & Mission
        item {
            GlowCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "HR Viral AI",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Create Smarter. Grow Organically.",
                        fontSize = 13.sp,
                        color = NeonPurple,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Version 1.0.0 (Native Android Compose)",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun IntegrationStatusCard(
    serviceName: String,
    statusText: String,
    statusDetail: String,
    isConnected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    GlowCard(borderColor = if (isConnected) NeonGreen.copy(alpha = 0.4f) else DarkNavyBorder) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = serviceName,
                        tint = if (isConnected) NeonGreen else NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = serviceName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Surface(
                    color = if (isConnected) Color(0x3310B981) else Color(0x33F59E0B),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (isConnected) "CONNECTED" else "STANDALONE",
                        color = if (isConnected) NeonGreen else NeonAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = statusText,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isConnected) NeonGreen else NeonAmber
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = statusDetail,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}
