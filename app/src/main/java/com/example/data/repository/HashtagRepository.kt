package com.example.data.repository

import com.example.data.model.CaptionStyle
import com.example.data.model.ContentLanguage
import com.example.data.model.CreatorNiche
import com.example.data.model.HashtagGroup
import com.example.data.model.HashtagSuggestion
import com.example.data.remote.GeminiClient

class HashtagRepository {

    suspend fun generateHashtags(
        topic: String,
        niche: CreatorNiche,
        language: ContentLanguage,
        audience: String,
        style: CaptionStyle
    ): HashtagGroup {
        val prompt = """
            You are an expert Instagram Reel algorithm specialist for Indian and global creators.
            Topic: "$topic"
            Creator Niche: "${niche.displayNameEn}"
            Language: "${language.displayName}"
            Target Audience: "$audience"
            Content Style: "${style.displayNameEn}"

            Generate 20 relevant, non-spammy Instagram hashtags divided into 4 specific categories:
            1. Broad Topic Tags (General high-volume discovery tags related to Reels and viral content)
            2. Specific Niche Tags (Targeted tags specifically for this exact topic and niche)
            3. Community Tags (Active creator engagement and peer tags)
            4. Language/Regional Tags (Tags reflecting the language and geographic audience like Hindi, India, etc.)

            Return ONLY the hashtags formatted in 4 clearly marked sections:
            [BROAD]
            #tag1 #tag2 #tag3 #tag4 #tag5
            [NICHE]
            #tag6 #tag7 #tag8 #tag9 #tag10
            [COMMUNITY]
            #tag11 #tag12 #tag13 #tag14 #tag15
            [REGIONAL]
            #tag16 #tag17 #tag18 #tag19 #tag20
        """.trimIndent()

        val systemPrompt = "You are an Instagram Reels hashtag strategist. Provide optimized, categorized hashtags. Avoid banned or repetitive spam tags."

        val aiResult = GeminiClient.generateWithGemini(systemPrompt, prompt)

        return if (aiResult.isSuccess) {
            parseAiHashtags(aiResult.getOrThrow(), topic, niche)
        } else {
            generateAlgorithmicHashtags(topic, niche, language)
        }
    }

    private fun parseAiHashtags(rawText: String, topic: String, niche: CreatorNiche): HashtagGroup {
        val broad = mutableListOf<HashtagSuggestion>()
        val nicheTags = mutableListOf<HashtagSuggestion>()
        val community = mutableListOf<HashtagSuggestion>()
        val regional = mutableListOf<HashtagSuggestion>()

        var currentSection = "BROAD"

        rawText.lines().forEach { line ->
            val trimmed = line.trim()
            when {
                trimmed.contains("[BROAD]", ignoreCase = true) -> currentSection = "BROAD"
                trimmed.contains("[NICHE]", ignoreCase = true) -> currentSection = "NICHE"
                trimmed.contains("[COMMUNITY]", ignoreCase = true) -> currentSection = "COMMUNITY"
                trimmed.contains("[REGIONAL]", ignoreCase = true) -> currentSection = "REGIONAL"
                else -> {
                    val tokens = trimmed.split(" ", "\t", ",").filter { it.startsWith("#") || it.isNotBlank() }
                    tokens.forEach { token ->
                        val cleanTag = if (token.startsWith("#")) token else "#$token"
                        val formattedTag = cleanTag.replace(Regex("[^a-zA-Z0-9_#]"), "").lowercase()
                        if (formattedTag.length > 2) {
                            when (currentSection) {
                                "BROAD" -> broad.add(HashtagSuggestion(formattedTag, "Broad Topic", "High Reach", 92))
                                "NICHE" -> nicheTags.add(HashtagSuggestion(formattedTag, "Specific Niche", "High Relevance", 95))
                                "COMMUNITY" -> community.add(HashtagSuggestion(formattedTag, "Creator Community", "Active Engagement", 88))
                                "REGIONAL" -> regional.add(HashtagSuggestion(formattedTag, "Regional Audience", "Targeted Region", 90))
                            }
                        }
                    }
                }
            }
        }

        // Fallback if parsing failed or was sparse
        if (broad.isEmpty() && nicheTags.isEmpty()) {
            return generateAlgorithmicHashtags(topic, niche, ContentLanguage.HINDI)
        }

        return HashtagGroup(
            broadTopicTags = broad.distinctBy { it.tag }.take(6),
            specificNicheTags = nicheTags.distinctBy { it.tag }.take(6),
            communityTags = community.distinctBy { it.tag }.take(5),
            regionalLanguageTags = regional.distinctBy { it.tag }.take(5),
            note = "AI-optimized algorithmic suggestions tailored to '$topic'."
        )
    }

    fun generateAlgorithmicHashtags(
        topic: String,
        niche: CreatorNiche,
        language: ContentLanguage
    ): HashtagGroup {
        val cleanTopic = topic.lowercase().replace(Regex("[^a-zA-Z0-9]"), "")
        val topicTag = if (cleanTopic.isNotBlank()) "#$cleanTopic" else "#reelsgrowth"

        val broad = listOf(
            HashtagSuggestion("#reelsindia", "Broad Topic", "High Reach", 95),
            HashtagSuggestion("#trendingreels", "Broad Topic", "High Reach", 94),
            HashtagSuggestion("#reelsinstagram", "Broad Topic", "High Reach", 91),
            HashtagSuggestion("#explorepage", "Broad Topic", "High Reach", 89),
            HashtagSuggestion("#viralreels", "Broad Topic", "High Reach", 92)
        )

        val nicheSpecific = when (niche) {
            CreatorNiche.TECH_AI -> listOf(
                HashtagSuggestion(topicTag, "Specific Niche", "High Relevance", 98),
                HashtagSuggestion("#techreels", "Specific Niche", "Targeted", 94),
                HashtagSuggestion("#aitools", "Specific Niche", "Targeted", 93),
                HashtagSuggestion("#technewsindia", "Specific Niche", "Targeted", 90),
                HashtagSuggestion("#learnai", "Specific Niche", "Targeted", 89)
            )
            CreatorNiche.ENTERTAINMENT -> listOf(
                HashtagSuggestion(topicTag, "Specific Niche", "High Relevance", 98),
                HashtagSuggestion("#comedyreels", "Specific Niche", "Targeted", 95),
                HashtagSuggestion("#desimemes", "Specific Niche", "Targeted", 92),
                HashtagSuggestion("#relatable", "Specific Niche", "Targeted", 91),
                HashtagSuggestion("#bakchodi", "Specific Niche", "Targeted", 88)
            )
            CreatorNiche.FITNESS -> listOf(
                HashtagSuggestion(topicTag, "Specific Niche", "High Relevance", 98),
                HashtagSuggestion("#fitnessindia", "Specific Niche", "Targeted", 94),
                HashtagSuggestion("#homeworkout", "Specific Niche", "Targeted", 92),
                HashtagSuggestion("#dietplan", "Specific Niche", "Targeted", 90),
                HashtagSuggestion("#gymmotivation", "Specific Niche", "Targeted", 89)
            )
            CreatorNiche.DEVOTIONAL -> listOf(
                HashtagSuggestion(topicTag, "Specific Niche", "High Relevance", 98),
                HashtagSuggestion("#bhaktireels", "Specific Niche", "Targeted", 96),
                HashtagSuggestion("#sanatandharma", "Specific Niche", "Targeted", 94),
                HashtagSuggestion("#mahadev", "Specific Niche", "Targeted", 93),
                HashtagSuggestion("#shrikrishna", "Specific Niche", "Targeted", 91)
            )
            CreatorNiche.FINANCE -> listOf(
                HashtagSuggestion(topicTag, "Specific Niche", "High Relevance", 98),
                HashtagSuggestion("#financeindia", "Specific Niche", "Targeted", 95),
                HashtagSuggestion("#moneytips", "Specific Niche", "Targeted", 93),
                HashtagSuggestion("#stockmarketindia", "Specific Niche", "Targeted", 91),
                HashtagSuggestion("#financialfreedom", "Specific Niche", "Targeted", 89)
            )
            else -> listOf(
                HashtagSuggestion(topicTag, "Specific Niche", "High Relevance", 98),
                HashtagSuggestion("#creatorsofinstagram", "Specific Niche", "Targeted", 92),
                HashtagSuggestion("#contentcreator", "Specific Niche", "Targeted", 90),
                HashtagSuggestion("#tipsandtricks", "Specific Niche", "Targeted", 89),
                HashtagSuggestion("#viralvideo", "Specific Niche", "Targeted", 88)
            )
        }

        val community = listOf(
            HashtagSuggestion("#indiancreators", "Creator Community", "Active Engagement", 91),
            HashtagSuggestion("#creatorshub", "Creator Community", "Active Engagement", 89),
            HashtagSuggestion("#creatorsgram", "Creator Community", "Active Engagement", 87),
            HashtagSuggestion("#reelslovers", "Creator Community", "Active Engagement", 86)
        )

        val regional = when (language) {
            ContentLanguage.HINDI -> listOf(
                HashtagSuggestion("#hindicreators", "Regional Audience", "Targeted Region", 93),
                HashtagSuggestion("#desireels", "Regional Audience", "Targeted Region", 91),
                HashtagSuggestion("#hindishayari", "Regional Audience", "Targeted Region", 88),
                HashtagSuggestion("#bharat", "Regional Audience", "Targeted Region", 87)
            )
            ContentLanguage.HINGLISH -> listOf(
                HashtagSuggestion("#hinglishreels", "Regional Audience", "Targeted Region", 92),
                HashtagSuggestion("#desicontent", "Regional Audience", "Targeted Region", 90),
                HashtagSuggestion("#indiagram", "Regional Audience", "Targeted Region", 89),
                HashtagSuggestion("#delhigram", "Regional Audience", "Targeted Region", 87)
            )
            ContentLanguage.ENGLISH -> listOf(
                HashtagSuggestion("#globalcreators", "Regional Audience", "Targeted Region", 91),
                HashtagSuggestion("#creatorrevolution", "Regional Audience", "Targeted Region", 89),
                HashtagSuggestion("#instaexplore", "Regional Audience", "Targeted Region", 88)
            )
        }

        return HashtagGroup(
            broadTopicTags = broad,
            specificNicheTags = nicheSpecific,
            communityTags = community,
            regionalLanguageTags = regional,
            note = "Algorithmically clustered suggestions. Keep total hashtags between 5-15 per Reel for best search indexation."
        )
    }
}
