package com.example.data.model

data class TrendItem(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val niche: CreatorNiche = CreatorNiche.ENTERTAINMENT,
    val popularityScore: Int = 85, // 0-100 relative trend momentum
    val sourceType: SourceType = SourceType.ALGORITHM_ANALYSIS,
    val sourceUrl: String? = null,
    val fetchedAt: Long = System.currentTimeMillis(),
    val verificationStatus: VerificationStatus = VerificationStatus.PUBLIC_SOURCE,
    val expiryTime: Long? = null,
    val hookIdea: String = "",
    val hashtags: List<String> = emptyList()
)

data class AudioItem(
    val id: String = "",
    val title: String = "",
    val artist: String = "Various Artists",
    val language: ContentLanguage = ContentLanguage.HINDI,
    val category: String = "Trending Music",
    val originalReelsUrl: String = "https://www.instagram.com/reels/audio/",
    val audioDurationSeconds: Int = 30,
    val momentumRank: Int = 1,
    val vibeTag: String = "Viral Beat",
    val sourceType: SourceType = SourceType.INSTAGRAM_AUDIO,
    val fetchedAt: Long = System.currentTimeMillis(),
    val verificationStatus: VerificationStatus = VerificationStatus.VERIFIED_LIVE,
    val isSaved: Boolean = false,
    val recommendedNiches: List<String> = listOf("All Creators", "Dance", "Vlogs", "Comedy")
)

data class HashtagSuggestion(
    val tag: String = "",
    val category: String = "Broad", // Broad, Specific, Community, Language
    val estimatedReachTier: String = "High Engagement",
    val relevanceScore: Int = 90
)

data class HashtagGroup(
    val broadTopicTags: List<HashtagSuggestion> = emptyList(),
    val specificNicheTags: List<HashtagSuggestion> = emptyList(),
    val communityTags: List<HashtagSuggestion> = emptyList(),
    val regionalLanguageTags: List<HashtagSuggestion> = emptyList(),
    val note: String = "Tags selected to optimize initial explore distribution."
)

data class GeneratedCaption(
    val id: String = "",
    val captionText: String = "",
    val firstThreeSecondsHook: String = "",
    val callToAction: String = "",
    val hashtags: List<String> = emptyList(),
    val language: ContentLanguage = ContentLanguage.HINDI,
    val style: CaptionStyle = CaptionStyle.SHORT_PUNCHY,
    val createdAt: Long = System.currentTimeMillis()
)

data class ScriptScene(
    val sceneNumber: Int = 1,
    val timeCode: String = "0:00 - 0:03",
    val visualAction: String = "",
    val onScreenText: String = "",
    val voiceoverOrDialogue: String = ""
)

data class ReelScript(
    val id: String = "",
    val title: String = "",
    val estimatedDurationSeconds: Int = 30,
    val targetNiche: CreatorNiche = CreatorNiche.ENTERTAINMENT,
    val openingHook: String = "",
    val scenes: List<ScriptScene> = emptyList(),
    val ctaClosing: String = "",
    val audioRecommendation: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class ContentPlan(
    val id: String = "",
    val dayNumber: Int = 1,
    val dayTitle: String = "Day 1: High Curiosity Hook",
    val format: String = "Talking Head Reel + B-roll",
    val topicDescription: String = "",
    val recommendedPostHourIST: String = "7:00 PM IST",
    val hookIdea: String = ""
)

data class AnalyticsSnapshot(
    val id: Long = 0,
    val reelTitle: String = "",
    val niche: CreatorNiche = CreatorNiche.ENTERTAINMENT,
    val views: Int = 0,
    val watchTimePercentage: Float = 0f,
    val likes: Int = 0,
    val comments: Int = 0,
    val shares: Int = 0,
    val saves: Int = 0,
    val loggedAt: Long = System.currentTimeMillis(),
    val coachScore: Int = 0,
    val keyRecommendation: String = ""
)

data class UserProfile(
    val name: String = "Creator",
    val instagramHandle: String = "@creator",
    val primaryNiche: CreatorNiche = CreatorNiche.ENTERTAINMENT,
    val preferredLanguage: ContentLanguage = ContentLanguage.HINDI,
    val isAutoBilingual: Boolean = true,
    val hasApiKeyConfigured: Boolean = false
)

data class DataSourceStatus(
    val sourceName: String = "",
    val isConnected: Boolean = false,
    val lastSyncTime: String = "",
    val statusMessage: String = "",
    val requiresAction: Boolean = false,
    val actionHint: String = ""
)
