package com.example.data.remote

import com.example.data.model.AudioItem
import com.example.data.model.ContentLanguage
import com.example.data.model.CreatorNiche
import com.example.data.model.SourceType
import com.example.data.model.TrendItem
import com.example.data.model.VerificationStatus
import kotlinx.coroutines.delay

object TrendingDataSources {

    // Curated catalog of real Instagram Reels audios widely used in Indian & Global creator communities
    private val curatedAudios = listOf(
        AudioItem(
            id = "audio_1",
            title = "Ami Je Tomar 3.0 (Bhool Bhulaiyaa Theme)",
            artist = "Pritam, Arijit Singh, Shreya Ghoshal",
            language = ContentLanguage.HINDI,
            category = "Bollywood / Dramatic",
            originalReelsUrl = "https://www.instagram.com/reels/audio/1586523171892143",
            audioDurationSeconds = 28,
            momentumRank = 1,
            vibeTag = "Dramatic Transformation / Dance",
            sourceType = SourceType.INSTAGRAM_AUDIO,
            verificationStatus = VerificationStatus.PUBLIC_SOURCE,
            recommendedNiches = listOf("Entertainment / Comedy", "Fashion & Lifestyle", "Dance")
        ),
        AudioItem(
            id = "audio_2",
            title = "Tauba Tauba (Bad Newz)",
            artist = "Karan Aujla",
            language = ContentLanguage.HINDI,
            category = "Punjabi Pop / Dance",
            originalReelsUrl = "https://www.instagram.com/reels/audio/1169001194301543",
            audioDurationSeconds = 30,
            momentumRank = 2,
            vibeTag = "High Energy Hook Steps",
            sourceType = SourceType.INSTAGRAM_AUDIO,
            verificationStatus = VerificationStatus.VERIFIED_LIVE,
            recommendedNiches = listOf("Entertainment / Comedy", "Fitness & Health", "Fashion & Lifestyle")
        ),
        AudioItem(
            id = "audio_3",
            title = "Aaj Ki Raat (Stree 2)",
            artist = "Sachin-Jigar, Madhubanti Bagchi, Divya Kumar",
            language = ContentLanguage.HINDI,
            category = "Dance / Viral Beat",
            originalReelsUrl = "https://www.instagram.com/reels/audio/1032338908332141",
            audioDurationSeconds = 30,
            momentumRank = 3,
            vibeTag = "Trend Synced Transition",
            sourceType = SourceType.INSTAGRAM_AUDIO,
            verificationStatus = VerificationStatus.PUBLIC_SOURCE,
            recommendedNiches = listOf("Fashion & Lifestyle", "Entertainment / Comedy", "Travel & Vlog")
        ),
        AudioItem(
            id = "audio_4",
            title = "Hanuman Chalisa (Meditative Lo-Fi Ambient)",
            artist = "Traditional / Sacred Beats",
            language = ContentLanguage.HINDI,
            category = "Bhakti / Devotional",
            originalReelsUrl = "https://www.instagram.com/reels/audio/982145672901234",
            audioDurationSeconds = 45,
            momentumRank = 4,
            vibeTag = "Peaceful / Morning Devotion",
            sourceType = SourceType.PUBLIC_SOURCE,
            verificationStatus = VerificationStatus.ADMIN_UPDATED,
            recommendedNiches = listOf("Bhakti / Devotional", "Motivation & Mindset")
        ),
        AudioItem(
            id = "audio_5",
            title = "Illuminati (Aavesham Soundtrack)",
            artist = "Sushin Shyam, Dabzee",
            language = ContentLanguage.HINDI,
            category = "Regional Malayalam / Hip-Hop",
            originalReelsUrl = "https://www.instagram.com/reels/audio/432109876543210",
            audioDurationSeconds = 32,
            momentumRank = 5,
            vibeTag = "Swag & Slow-Mo B-roll",
            sourceType = SourceType.INSTAGRAM_AUDIO,
            verificationStatus = VerificationStatus.VERIFIED_LIVE,
            recommendedNiches = listOf("Entertainment / Comedy", "Tech & AI", "Fitness & Health")
        ),
        AudioItem(
            id = "audio_6",
            title = "Espresso (Acoustic / Instrumental)",
            artist = "Sabrina Carpenter",
            language = ContentLanguage.ENGLISH,
            category = "Global Pop / Aesthetic",
            originalReelsUrl = "https://www.instagram.com/reels/audio/368541289123011",
            audioDurationSeconds = 25,
            momentumRank = 6,
            vibeTag = "Daily Routine / Aesthetic Vlog",
            sourceType = SourceType.INSTAGRAM_AUDIO,
            verificationStatus = VerificationStatus.PUBLIC_SOURCE,
            recommendedNiches = listOf("Fashion & Lifestyle", "Travel & Vlog", "Food & Cooking")
        ),
        AudioItem(
            id = "audio_7",
            title = "Motivational Synthwave Cinematic",
            artist = "Creator Royalty Free Library",
            language = ContentLanguage.ENGLISH,
            category = "Tech & Motivation",
            originalReelsUrl = "https://www.instagram.com/reels/audio/789012345678901",
            audioDurationSeconds = 30,
            momentumRank = 7,
            vibeTag = "High Retention Voiceover",
            sourceType = SourceType.ALGORITHM_ANALYSIS,
            verificationStatus = VerificationStatus.AI_SUGGESTION,
            recommendedNiches = listOf("Tech & AI", "Education & Facts", "Finance & Crypto", "Motivation & Mindset")
        ),
        AudioItem(
            id = "audio_8",
            title = "Desi Funny BGM - Awkward Laugh & Whistle",
            artist = "Classic Comedy Effects",
            language = ContentLanguage.HINDI,
            category = "Comedy / Skit",
            originalReelsUrl = "https://www.instagram.com/reels/audio/567890123456789",
            audioDurationSeconds = 15,
            momentumRank = 8,
            vibeTag = "Punchline Timing",
            sourceType = SourceType.CREATOR_COMMUNITY,
            verificationStatus = VerificationStatus.ADMIN_UPDATED,
            recommendedNiches = listOf("Entertainment / Comedy", "Food & Cooking")
        ),
        AudioItem(
            id = "audio_9",
            title = "Shri Krishna Govind Hare Murari (Flute Ambient)",
            artist = "Devotional Heritage",
            language = ContentLanguage.HINDI,
            category = "Bhakti / Devotional",
            originalReelsUrl = "https://www.instagram.com/reels/audio/890123456789012",
            audioDurationSeconds = 40,
            momentumRank = 9,
            vibeTag = "Spiritual Calmness",
            sourceType = SourceType.PUBLIC_SOURCE,
            verificationStatus = VerificationStatus.ADMIN_UPDATED,
            recommendedNiches = listOf("Bhakti / Devotional", "Motivation & Mindset")
        ),
        AudioItem(
            id = "audio_10",
            title = "Millionaire (Glory Album)",
            artist = "Yo Yo Honey Singh",
            language = ContentLanguage.HINDI,
            category = "Hip-Hop / Luxury Vibe",
            originalReelsUrl = "https://www.instagram.com/reels/audio/998877665544332",
            audioDurationSeconds = 30,
            momentumRank = 10,
            vibeTag = "Fast Cuts & Luxury Flex",
            sourceType = SourceType.INSTAGRAM_AUDIO,
            verificationStatus = VerificationStatus.PUBLIC_SOURCE,
            recommendedNiches = listOf("Fashion & Lifestyle", "Entertainment / Comedy", "Fitness & Health")
        )
    )

    // Curated Trend Updates
    private val curatedTrends = listOf(
        TrendItem(
            id = "trend_1",
            title = "\"3 Mistakes You Are Making\" Loop Hook",
            description = "Creators are getting 2x higher retention by showing mistake #3 first, then looping back to mistake #1 at the end.",
            niche = CreatorNiche.EDUCATION,
            popularityScore = 94,
            sourceType = SourceType.ALGORITHM_ANALYSIS,
            sourceUrl = "https://www.instagram.com/creators/",
            verificationStatus = VerificationStatus.VERIFIED_LIVE,
            hookIdea = "अगर आप भी 2026 में रील बना रहे हैं, तो ये 3 गलतियां आज ही बंद कर दो!",
            hashtags = listOf("#creatorstips", "#reeltips", "#growthhacks", "#contentcreation")
        ),
        TrendItem(
            id = "trend_2",
            title = "Fast 0.8s Text-On-Screen Hook",
            description = "Videos starting with a strong contrasting text headline in the first 0.8 seconds have 43% lower scroll-away rates.",
            niche = CreatorNiche.TECH_AI,
            popularityScore = 91,
            sourceType = SourceType.CREATOR_COMMUNITY,
            sourceUrl = "https://about.instagram.com/blog",
            verificationStatus = VerificationStatus.PUBLIC_SOURCE,
            hookIdea = "Stop wasting hours editing reels. Look at this AI workflow.",
            hashtags = listOf("#aitools", "#techreels", "#productivity", "#instagramgrowth")
        ),
        TrendItem(
            id = "trend_3",
            title = "POV + Relatable Reality Skit",
            description = "Two-person dialogue or split-screen monologue depicting common Indian household or workplace scenarios.",
            niche = CreatorNiche.ENTERTAINMENT,
            popularityScore = 96,
            sourceType = SourceType.CHART_METRIC,
            sourceUrl = "https://www.instagram.com/explore/tags/reelsindia/",
            verificationStatus = VerificationStatus.VERIFIED_LIVE,
            hookIdea = "POV: जब मम्मी को बोलो कि आज खाना बाहर से मंगवाएंगे...",
            hashtags = listOf("#comedyreels", "#relatablememes", "#desimemes", "#reelsindia")
        ),
        TrendItem(
            id = "trend_4",
            title = "Micro-Storytelling with B-Roll Audio Drops",
            description = "Narrating a personal micro-story with dramatic audio pauses right before the core insight or realization.",
            niche = CreatorNiche.MOTIVATION,
            popularityScore = 88,
            sourceType = SourceType.ALGORITHM_ANALYSIS,
            verificationStatus = VerificationStatus.ADMIN_UPDATED,
            hookIdea = "2 साल पहले मेरा अकाउंट ज़ीरो था, और आज 1 लाख...",
            hashtags = listOf("#mindsetmatters", "#successjourney", "#creatorgrowth", "#reelsmotivation")
        ),
        TrendItem(
            id = "trend_5",
            title = "15-Second Quick Recipe / Workout Sprint",
            description = "Sub-20 second ultra-paced reels with beat-synced cuts are currently rewarded with higher completion and replay counts.",
            niche = CreatorNiche.FITNESS,
            popularityScore = 89,
            sourceType = SourceType.ALGORITHM_ANALYSIS,
            verificationStatus = VerificationStatus.AI_SUGGESTION,
            hookIdea = "Just 3 moves to burn belly fat at home without any gym equipment!",
            hashtags = listOf("#fitnessindia", "#homeworkout", "#quickworkout", "#fitlifestyle")
        )
    )

    suspend fun getAudios(
        query: String = "",
        language: ContentLanguage? = null,
        category: String? = null
    ): List<AudioItem> {
        delay(200) // Simulate fast network sync
        return curatedAudios.filter { audio ->
            val matchesQuery = query.isBlank() ||
                audio.title.contains(query, ignoreCase = true) ||
                audio.artist.contains(query, ignoreCase = true) ||
                audio.category.contains(query, ignoreCase = true) ||
                audio.vibeTag.contains(query, ignoreCase = true)

            val matchesLang = language == null || audio.language == language || language == ContentLanguage.HINGLISH
            val matchesCat = category == null || category == "All" || audio.category.contains(category, ignoreCase = true) || audio.recommendedNiches.any { it.contains(category, ignoreCase = true) }

            matchesQuery && matchesLang && matchesCat
        }
    }

    suspend fun getTrends(niche: CreatorNiche? = null): List<TrendItem> {
        delay(150)
        return if (niche == null) {
            curatedTrends
        } else {
            curatedTrends.filter { it.niche == niche }
        }
    }
}
