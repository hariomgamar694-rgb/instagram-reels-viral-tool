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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.local.SavedItemEntity
import com.example.data.model.ContentLanguage
import com.example.data.model.SavedItemType
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
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.SavedVaultViewModel
import com.example.util.ShareUtils

@Composable
fun SavedVaultScreen(
    vaultViewModel: SavedVaultViewModel,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLang by mainViewModel.currentLanguage.collectAsStateWithLifecycle()
    val isHindi = currentLang == ContentLanguage.HINDI

    val savedItems by vaultViewModel.allSavedItems.collectAsStateWithLifecycle()
    val filterType by vaultViewModel.selectedFilterType.collectAsStateWithLifecycle()

    val filteredList = if (filterType == null) {
        savedItems
    } else {
        savedItems.filter { it.type == filterType?.name }
    }

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
                title = if (isHindi) "सेव्ड कंटेंट वॉल्ट" else "Saved Content Vault",
                subtitle = if (isHindi) "आपके सहेजे गए कैप्शन, स्क्रिप्ट, हैशटैग और ऑडियो" else "Your stored captions, scripts, hashtags & audio",
                badgeText = "${savedItems.size} Total"
            )
        }

        // Filter Type Pills
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    val isSelected = filterType == null
                    Surface(
                        color = if (isSelected) NeonPurple else DarkNavySurface,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) NeonPurple else DarkNavyBorder
                        ),
                        modifier = Modifier.clickable { vaultViewModel.setFilterType(null) }
                    ) {
                        Text(
                            text = if (isHindi) "सभी (${savedItems.size})" else "All (${savedItems.size})",
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                items(SavedItemType.values()) { type ->
                    val isSelected = filterType == type
                    val count = savedItems.count { it.type == type.name }
                    Surface(
                        color = if (isSelected) NeonPurple else DarkNavySurface,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) NeonPurple else DarkNavyBorder
                        ),
                        modifier = Modifier.clickable { vaultViewModel.setFilterType(type) }
                    ) {
                        Text(
                            text = "${type.name.replace("_", " ")} ($count)",
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        if (filteredList.isEmpty()) {
            item {
                GlowCard {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Empty",
                            tint = TextMuted,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isHindi) "वॉल्ट अभी खाली है" else "Your Vault is Empty",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isHindi) "कैप्शन, हैशटैग या ऑडियो जनरेट करते समय 'Save' बटन दबाएं।" else "Tap the 'Save' button in AI Writer, Hashtags or Audio to store content here.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        } else {
            items(filteredList) { item ->
                SavedVaultCard(
                    item = item,
                    onCopy = { ShareUtils.copyToClipboard(context, item.content, item.title) },
                    onShare = { ShareUtils.shareText(context, item.content, item.title) },
                    onDelete = {
                        vaultViewModel.deleteItem(item.id)
                        Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
private fun SavedVaultCard(
    item: SavedItemEntity,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit
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
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = item.type.replace("_", " "),
                        color = NeonPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = NeonGreen,
                            modifier = Modifier.size(16.dp)
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

            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                color = DarkNavySurface,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.content,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}
