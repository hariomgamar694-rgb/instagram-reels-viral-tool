package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.ContentLanguage
import com.example.ui.screens.AiWriterScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.AudioScreen
import com.example.ui.screens.GrowthCoachScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HashtagScreen
import com.example.ui.screens.PostingTimeScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.SavedVaultScreen
import com.example.ui.theme.DarkNavyBackground
import com.example.ui.theme.DarkNavyBorder
import com.example.ui.theme.DarkNavySurface
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.AiWriterViewModel
import com.example.ui.viewmodel.AnalyticsViewModel
import com.example.ui.viewmodel.AudioViewModel
import com.example.ui.viewmodel.GrowthCoachViewModel
import com.example.ui.viewmodel.HashtagViewModel
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.PostingTimeViewModel
import com.example.ui.viewmodel.SavedVaultViewModel
import com.example.util.LanguageManager

enum class ScreenDestination {
    HOME,
    AUDIO,
    HASHTAGS,
    AI_WRITER,
    GROWTH_COACH,
    POSTING_TIME,
    ANALYTICS,
    SAVED_VAULT,
    SETTINGS
}

data class NavItem(
    val destination: ScreenDestination,
    val titleKey: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = viewModel(),
    audioViewModel: AudioViewModel = viewModel(),
    hashtagViewModel: HashtagViewModel = viewModel(),
    writerViewModel: AiWriterViewModel = viewModel(),
    coachViewModel: GrowthCoachViewModel = viewModel(),
    postingViewModel: PostingTimeViewModel = viewModel(),
    analyticsViewModel: AnalyticsViewModel = viewModel(),
    vaultViewModel: SavedVaultViewModel = viewModel()
) {
    var currentScreen by rememberSaveable { mutableStateOf(ScreenDestination.HOME) }
    val currentLang by mainViewModel.currentLanguage.collectAsStateWithLifecycle()

    // Handle back button on sub-screens to return to Home
    if (currentScreen != ScreenDestination.HOME) {
        BackHandler {
            currentScreen = ScreenDestination.HOME
        }
    }

    val bottomNavItems = listOf(
        NavItem(ScreenDestination.HOME, "nav_home", Icons.Default.Home, "nav_item_home"),
        NavItem(ScreenDestination.AUDIO, "nav_audio", Icons.Default.MusicNote, "nav_item_audio"),
        NavItem(ScreenDestination.HASHTAGS, "nav_hashtags", Icons.Default.Tag, "nav_item_hashtags"),
        NavItem(ScreenDestination.AI_WRITER, "nav_ai_writer", Icons.Default.AutoAwesome, "nav_item_ai_writer"),
        NavItem(ScreenDestination.GROWTH_COACH, "nav_coach", Icons.Default.TrendingUp, "nav_item_coach")
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = DarkNavySurface,
                tonalElevation = 8.dp
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected = currentScreen == item.destination
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentScreen = item.destination },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = LanguageManager.getTitle(item.titleKey, currentLang),
                                tint = if (isSelected) NeonPurple else TextMuted
                            )
                        },
                        label = {
                            Text(
                                text = LanguageManager.getTitle(item.titleKey, currentLang),
                                fontSize = 10.sp,
                                color = if (isSelected) TextPrimary else TextMuted
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonPurple,
                            indicatorColor = Color(0x338B5CF6)
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                ScreenDestination.HOME -> HomeScreen(
                    mainViewModel = mainViewModel,
                    onNavigateToAudio = { currentScreen = ScreenDestination.AUDIO },
                    onNavigateToHashtags = { currentScreen = ScreenDestination.HASHTAGS },
                    onNavigateToAiWriter = { currentScreen = ScreenDestination.AI_WRITER },
                    onNavigateToCoach = { currentScreen = ScreenDestination.GROWTH_COACH },
                    onNavigateToPostingTime = { currentScreen = ScreenDestination.POSTING_TIME },
                    onNavigateToAnalytics = { currentScreen = ScreenDestination.ANALYTICS },
                    onNavigateToSavedVault = { currentScreen = ScreenDestination.SAVED_VAULT },
                    onNavigateToSettings = { currentScreen = ScreenDestination.SETTINGS }
                )

                ScreenDestination.AUDIO -> AudioScreen(
                    audioViewModel = audioViewModel,
                    mainViewModel = mainViewModel
                )

                ScreenDestination.HASHTAGS -> HashtagScreen(
                    hashtagViewModel = hashtagViewModel,
                    mainViewModel = mainViewModel
                )

                ScreenDestination.AI_WRITER -> AiWriterScreen(
                    writerViewModel = writerViewModel,
                    mainViewModel = mainViewModel
                )

                ScreenDestination.GROWTH_COACH -> GrowthCoachScreen(
                    coachViewModel = coachViewModel,
                    mainViewModel = mainViewModel,
                    onNavigateToTracker = { currentScreen = ScreenDestination.ANALYTICS }
                )

                ScreenDestination.POSTING_TIME -> PostingTimeScreen(
                    postingViewModel = postingViewModel,
                    mainViewModel = mainViewModel
                )

                ScreenDestination.ANALYTICS -> AnalyticsScreen(
                    analyticsViewModel = analyticsViewModel,
                    mainViewModel = mainViewModel,
                    onNavigateToCoach = { currentScreen = ScreenDestination.GROWTH_COACH }
                )

                ScreenDestination.SAVED_VAULT -> SavedVaultScreen(
                    vaultViewModel = vaultViewModel,
                    mainViewModel = mainViewModel
                )

                ScreenDestination.SETTINGS -> ProfileSettingsScreen(
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}
