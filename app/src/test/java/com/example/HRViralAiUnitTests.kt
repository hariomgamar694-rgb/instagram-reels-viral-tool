package com.example

import com.example.data.model.CaptionStyle
import com.example.data.model.ContentLanguage
import com.example.data.model.CreatorNiche
import com.example.data.repository.GrowthCoachRepository
import com.example.data.repository.HashtagRepository
import com.example.data.repository.PostingTimeRepository
import com.example.util.LanguageManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HRViralAiUnitTests {

    @Test
    fun growthCoach_highRetentionAndShares_givesHighScore() {
        val coach = GrowthCoachRepository()
        val result = coach.analyzeReelPerformance(
            niche = CreatorNiche.TECH_AI,
            goal = "Reach",
            views = 10000,
            watchTimePercentage = 88f,
            likes = 800,
            comments = 60,
            shares = 350, // 3.5% share ratio
            saves = 280   // 2.8% save ratio
        )

        assertTrue("Expected score >= 80, got ${result.overallHealthScore}", result.overallHealthScore >= 80)
        assertTrue("Expected strong hook, got ${result.hookRating}", result.hookRating.contains("Strong"))
        assertEquals(3.5f, result.shareToViewRatioPercent, 0.01f)
        assertEquals(2.8f, result.saveToViewRatioPercent, 0.01f)
    }

    @Test
    fun growthCoach_lowWatchTime_diagnosesEarlyDropOff() {
        val coach = GrowthCoachRepository()
        val result = coach.analyzeReelPerformance(
            niche = CreatorNiche.ENTERTAINMENT,
            goal = "Followers",
            views = 5000,
            watchTimePercentage = 25f, // very low watch time
            likes = 100,
            comments = 5,
            shares = 10,
            saves = 5
        )

        assertTrue("Expected score <= 50, got ${result.overallHealthScore}", result.overallHealthScore <= 50)
        assertTrue(result.hookRating.contains("Drop-Off") || result.hookRating.contains("Needs Work"))
    }

    @Test
    fun postingTime_devotionalNiche_prioritizesMorningSlot() {
        val repo = PostingTimeRepository()
        val rec = repo.getPostingRecommendations(CreatorNiche.DEVOTIONAL)

        assertNotNull(rec)
        assertTrue(rec.bestSlotMorningIST.contains("6:00 AM") || rec.bestSlotMorningIST.contains("Pratah"))
        assertTrue(rec.connectionStatusNote.contains("General Aggregated Benchmark"))
    }

    @Test
    fun hashtagRepository_algorithmicClustering_producesFourGroups() {
        val repo = HashtagRepository()
        val groups = repo.generateAlgorithmicHashtags(
            topic = "AI Tools",
            niche = CreatorNiche.TECH_AI,
            language = ContentLanguage.HINDI
        )

        assertTrue(groups.broadTopicTags.isNotEmpty())
        assertTrue(groups.specificNicheTags.isNotEmpty())
        assertTrue(groups.communityTags.isNotEmpty())
        assertTrue(groups.regionalLanguageTags.isNotEmpty())

        assertTrue(groups.broadTopicTags.any { it.tag.startsWith("#") })
        assertTrue(groups.specificNicheTags.any { it.tag.contains("tech") || it.tag.contains("ai") })
    }

    @Test
    fun languageManager_bilingualTranslations_work() {
        val hindiTitle = LanguageManager.getTitle("section_trending_audio", ContentLanguage.HINDI)
        val englishTitle = LanguageManager.getTitle("section_trending_audio", ContentLanguage.ENGLISH)

        assertEquals("ट्रेंडिंग ऑडियो डिस्कवरी", hindiTitle)
        assertEquals("Trending Audio Discovery", englishTitle)
    }
}
