package com.example.data.repository

import com.example.data.model.CaptionStyle
import com.example.data.model.ContentLanguage
import com.example.data.model.ContentPlan
import com.example.data.model.CreatorNiche
import com.example.data.model.GeneratedCaption
import com.example.data.model.ReelScript
import com.example.data.model.ScriptScene
import com.example.data.remote.GeminiClient

class CaptionScriptRepository {

    suspend fun generateCaption(
        topic: String,
        niche: CreatorNiche,
        language: ContentLanguage,
        style: CaptionStyle
    ): Result<GeneratedCaption> {
        val langInstruction = when (language) {
            ContentLanguage.HINDI -> "Write in natural conversational Hindi (Devanagari script) with punchy phrases."
            ContentLanguage.HINGLISH -> "Write in natural Hinglish (Roman script Hindi mixed with English keywords as popular on Indian Instagram)."
            ContentLanguage.ENGLISH -> "Write in crisp, engaging English."
        }

        val prompt = """
            You are a top Instagram viral Reels content strategist.
            Topic: "$topic"
            Niche: "${niche.displayNameEn}"
            Language: $langInstruction
            Caption Style: "${style.displayNameEn}"

            Generate:
            1. An irresistible First-3-Seconds Opening Hook that stops the scroll immediately.
            2. An engaging Reel Caption formatted with line breaks, emojis, and high readability.
            3. A strong Call-To-Action (CTA) that encourages comments or shares (e.g. "Comment 'GUIDE' below", "Save this before it disappears").
            4. 8 highly relevant hashtags.

            Respond with EXACTLY this structure:
            HOOK: [your 1-sentence viral hook here]
            CAPTION:
            [your formatted caption here]
            CTA: [your call to action here]
            HASHTAGS: #tag1 #tag2 #tag3 #tag4 #tag5 #tag6 #tag7 #tag8
        """.trimIndent()

        val systemPrompt = "You are HR Viral AI's master caption writer for Instagram Reels. Write compelling, human-sounding copy with high retention hooks."

        val aiResponse = GeminiClient.generateWithGemini(systemPrompt, prompt)

        return if (aiResponse.isSuccess) {
            val text = aiResponse.getOrThrow()
            Result.success(parseGeneratedCaption(text, language, style))
        } else {
            // Intelligent fallback template generator
            val fallback = generateFallbackCaption(topic, niche, language, style)
            Result.success(fallback)
        }
    }

    suspend fun modifyCaption(
        existingCaption: GeneratedCaption,
        action: String // "SHORTEN", "EXPAND", "REWRITE", "MAKE_FUNNY", "MAKE_INSPIRATIONAL"
    ): Result<GeneratedCaption> {
        val actionInstruction = when (action) {
            "SHORTEN" -> "Make this caption much shorter, punchier, and under 30 words for fast scrolling."
            "EXPAND" -> "Expand this caption with 3 bulleted insights, detailed tips, and value-packed context."
            "MAKE_FUNNY" -> "Rewrite this in a humorous, witty, self-deprecating tone with relatable Indian creator comedy."
            "MAKE_INSPIRATIONAL" -> "Rewrite this with a powerful motivational mindset tone that sparks ambition."
            else -> "Rewrite this caption with fresh hooks and alternate phrasing."
        }

        val prompt = """
            Original Caption:
            "${existingCaption.captionText}"
            
            Original Hook:
            "${existingCaption.firstThreeSecondsHook}"

            Instruction: $actionInstruction
            Maintain language: ${existingCaption.language.displayName}

            Respond strictly with:
            HOOK: [revised hook]
            CAPTION:
            [revised caption]
            CTA: [revised call to action]
            HASHTAGS: ${existingCaption.hashtags.joinToString(" ")}
        """.trimIndent()

        val aiResult = GeminiClient.generateWithGemini("You are a professional social media editor.", prompt)
        return if (aiResult.isSuccess) {
            Result.success(parseGeneratedCaption(aiResult.getOrThrow(), existingCaption.language, existingCaption.style))
        } else {
            // Quick local transformation fallback
            val newHook = when (action) {
                "SHORTEN" -> "⚡ Quick tip: ${existingCaption.firstThreeSecondsHook}"
                "MAKE_FUNNY" -> "😂 सच-सच बताओ: ${existingCaption.firstThreeSecondsHook}"
                "MAKE_INSPIRATIONAL" -> "🔥 याद रखना: ${existingCaption.firstThreeSecondsHook}"
                else -> "🚀 ${existingCaption.firstThreeSecondsHook}"
            }
            Result.success(
                existingCaption.copy(
                    firstThreeSecondsHook = newHook,
                    captionText = if (action == "SHORTEN") existingCaption.captionText.take(120) + "...\n\n👇 Read more in bio!" else existingCaption.captionText
                )
            )
        }
    }

    suspend fun generateScript(
        topic: String,
        niche: CreatorNiche,
        language: ContentLanguage,
        durationSeconds: Int = 30
    ): Result<ReelScript> {
        val langInstruction = when (language) {
            ContentLanguage.HINDI -> "Use conversational Hindi for voiceovers and English for visual cues."
            ContentLanguage.HINGLISH -> "Use Hinglish for voiceover and dialogue."
            ContentLanguage.ENGLISH -> "Use English for dialogue and instructions."
        }

        val prompt = """
            Generate a full 3-scene Instagram Reel Script for a $durationSeconds-second video.
            Topic: "$topic"
            Niche: "${niche.displayNameEn}"
            Language: $langInstruction

            Structure:
            SCENE 1 (0:00 - 0:03): High retention pattern-interrupt opening hook.
            SCENE 2 (0:03 - 0:22): Core value, story, or demonstration with visual actions and on-screen text.
            SCENE 3 (0:22 - 0:30): Climax / punchline and clear Call-To-Action (CTA).

            Respond with EXACTLY:
            TITLE: [catchy title]
            HOOK: [opening line spoken or shown]
            SCENE_1_ACTION: [visual direction]
            SCENE_1_TEXT: [text overlay on screen]
            SCENE_1_VO: [voiceover dialogue]
            SCENE_2_ACTION: [visual direction]
            SCENE_2_TEXT: [text overlay on screen]
            SCENE_2_VO: [voiceover dialogue]
            SCENE_3_ACTION: [visual direction]
            SCENE_3_TEXT: [text overlay on screen]
            SCENE_3_VO: [voiceover dialogue]
            CTA: [final CTA]
            AUDIO_VIBE: [recommended background music style]
        """.trimIndent()

        val aiResult = GeminiClient.generateWithGemini("You are a viral reel director and scriptwriter.", prompt)

        return if (aiResult.isSuccess) {
            Result.success(parseScript(aiResult.getOrThrow(), topic, niche, durationSeconds))
        } else {
            Result.success(generateFallbackScript(topic, niche, language, durationSeconds))
        }
    }

    suspend fun generateContentCalendar(
        niche: CreatorNiche,
        goal: String
    ): List<ContentPlan> {
        return listOf(
            ContentPlan(
                id = "plan_1",
                dayNumber = 1,
                dayTitle = "Day 1: The 'Stop Doing This' Hook",
                format = "Direct To Camera + Rapid Cutaways",
                topicDescription = "Address the single biggest misconception beginners have in ${niche.displayNameEn}.",
                recommendedPostHourIST = "7:30 PM IST",
                hookIdea = "Stop doing this if you want real results in ${niche.displayNameEn}!"
            ),
            ContentPlan(
                id = "plan_2",
                dayNumber = 2,
                dayTitle = "Day 2: 3 Secret Tools / Hacks",
                format = "Screen Recording with Picture-in-Picture Voiceover",
                topicDescription = "Showcase 3 tools or techniques that save 80% time or give an unfair advantage.",
                recommendedPostHourIST = "8:00 PM IST",
                hookIdea = "3 websites that feel completely illegal to know in 2026."
            ),
            ContentPlan(
                id = "plan_3",
                dayNumber = 3,
                dayTitle = "Day 3: Relatable Meme / Skit",
                format = "POV Split Screen Skit",
                topicDescription = "A lighthearted joke or situation that every person in your audience encounters.",
                recommendedPostHourIST = "1:30 PM IST (Lunch Peak)",
                hookIdea = "POV: When you try explaining your work to Indian relatives..."
            ),
            ContentPlan(
                id = "plan_4",
                dayNumber = 4,
                dayTitle = "Day 4: Step-by-Step Breakdown",
                format = "Carousel or 45-Second Fast Tutorial",
                topicDescription = "Deep actionable blueprint on solving one exact problem.",
                recommendedPostHourIST = "6:45 PM IST",
                hookIdea = "How I mastered this in just 14 days without spending money."
            ),
            ContentPlan(
                id = "plan_5",
                dayNumber = 5,
                dayTitle = "Day 5: Controversial Opinion / Myth Busting",
                format = "Text On Screen with Strong Eye-Contact",
                topicDescription = "Challenge a popular opinion in your industry to trigger engagement in the comments.",
                recommendedPostHourIST = "8:30 PM IST",
                hookIdea = "Unpopular opinion: Most creators are lying to you about this..."
            ),
            ContentPlan(
                id = "plan_6",
                dayNumber = 6,
                dayTitle = "Day 6: Behind The Scenes / Authenticity",
                format = "Aesthetic V-log with Trending Lo-Fi Music",
                topicDescription = "Show your actual workspace, process, failures, or honest workflow.",
                recommendedPostHourIST = "11:00 AM IST (Weekend Morning)",
                hookIdea = "What a realistic day of content creation actually looks like."
            ),
            ContentPlan(
                id = "plan_7",
                dayNumber = 7,
                dayTitle = "Day 7: High Value Freebie / Community Q&A",
                format = "Talking Head + Comment Overlay",
                topicDescription = "Answer the top 3 questions from your DM or comment section with a clear CTA.",
                recommendedPostHourIST = "7:00 PM IST (Sunday Evening)",
                hookIdea = "You asked, I answered: The honest truth about..."
            )
        )
    }

    private fun parseGeneratedCaption(
        raw: String,
        language: ContentLanguage,
        style: CaptionStyle
    ): GeneratedCaption {
        var hook = ""
        val captionLines = mutableListOf<String>()
        var cta = ""
        val hashtags = mutableListOf<String>()

        var section = "NONE"

        raw.lines().forEach { line ->
            val trimmed = line.trim()
            when {
                trimmed.startsWith("HOOK:", ignoreCase = true) -> {
                    hook = trimmed.substringAfter(":").trim()
                }
                trimmed.startsWith("CAPTION:", ignoreCase = true) -> {
                    section = "CAPTION"
                }
                trimmed.startsWith("CTA:", ignoreCase = true) -> {
                    section = "CTA"
                    cta = trimmed.substringAfter(":").trim()
                }
                trimmed.startsWith("HASHTAGS:", ignoreCase = true) -> {
                    section = "HASHTAGS"
                    val tagStr = trimmed.substringAfter(":")
                    hashtags.addAll(tagStr.split(" ").filter { it.startsWith("#") })
                }
                else -> {
                    if (section == "CAPTION" && !trimmed.startsWith("CTA:")) {
                        captionLines.add(line)
                    } else if (section == "HASHTAGS") {
                        hashtags.addAll(trimmed.split(" ").filter { it.startsWith("#") })
                    }
                }
            }
        }

        val captionBody = if (captionLines.isNotEmpty()) {
            captionLines.joinToString("\n").trim()
        } else {
            raw.take(300)
        }

        val finalHook = hook.ifBlank { "Stop scrolling! Look at this..." }
        val finalCta = cta.ifBlank { "Save this Reel & Share with a creator friend! 🚀" }
        val finalTags = if (hashtags.isNotEmpty()) hashtags else listOf("#reelsindia", "#trendingreels", "#creatorgrowth", "#viralreels")

        return GeneratedCaption(
            id = "cap_${System.currentTimeMillis()}",
            captionText = captionBody,
            firstThreeSecondsHook = finalHook,
            callToAction = finalCta,
            hashtags = finalTags,
            language = language,
            style = style
        )
    }

    private fun generateFallbackCaption(
        topic: String,
        niche: CreatorNiche,
        language: ContentLanguage,
        style: CaptionStyle
    ): GeneratedCaption {
        val (hook, body, cta) = when (language) {
            ContentLanguage.HINDI -> Triple(
                "अगर आप भी $topic में बेहतर बनना चाहते हैं, तो ये 3 चीज़ें आज ही नोट कर लो! ✍️",
                "रील देखने में समय बर्बाद मत करो, इसे लागू करो!\n\n1. पहली चीज़: कंसिस्टेंसी ही सबसे बड़ी चाबी है।\n2. दूसरी चीज़: शुरुआत के 3 सेकंड में हुक दो।\n3. तीसरी चीज़: ऑडियंस से सवाल पूछो ताकि कमेंट्स बढ़ें।\n\nकौन सा पॉइंट आपको सबसे सही लगा?",
                "अगर वैल्यू मिली हो तो इसे तुरंत SAVE कर लो ताकि भूल न जाओ! 📌"
            )
            ContentLanguage.HINGLISH -> Triple(
                "Stop scrolling! Agar aapka bhi $topic reach dead ho raha hai, read this carefully 👇",
                "Ye 3 simple tricks use karo aur result khud dekho:\n\n✨ Trick 1: Hook the viewer in first 2 seconds.\n✨ Trick 2: High quality visual pacing with beat-sync.\n✨ Trick 3: Strong Call To Action for saves and shares.\n\nAap isme se konsa try kar rahe ho?",
                "Comment 'VIRAL' below and I will send you the secret strategy! 🔥"
            )
            ContentLanguage.ENGLISH -> Triple(
                "The #1 mistake people make with $topic (and how to fix it fast) ⚡",
                "Most creators overcomplicate the process. Here is the realistic breakdown:\n\n• Step 1: Focus on watch time, not just vanity likes.\n• Step 2: Deliver the value promise immediately.\n• Step 3: Give people a real reason to hit save.\n\nSimplicity always wins on the algorithm.",
                "Hit SAVE to keep this handy for your next Reel shoot! 🎬"
            )
        }

        return GeneratedCaption(
            id = "cap_fallback_${System.currentTimeMillis()}",
            captionText = body,
            firstThreeSecondsHook = hook,
            callToAction = cta,
            hashtags = listOf("#$topic", "#reelsindia", "#trendingreels", "#creatorstrategy", "#viralcontent"),
            language = language,
            style = style
        )
    }

    private fun parseScript(
        raw: String,
        topic: String,
        niche: CreatorNiche,
        duration: Int
    ): ReelScript {
        var title = "Viral Reel on $topic"
        var hook = "Did you know this about $topic?"
        var s1Action = "Fast visual cut"
        var s1Text = "Stop doing this!"
        var s1Vo = "If you want results, listen closely."
        var s2Action = "Showing screen demonstration"
        var s2Text = "3 Golden Rules"
        var s2Vo = "Rule number one is clarity. Rule number two is consistency."
        var s3Action = "Looking straight at camera with confident smile"
        var s3Text = "Follow for daily tips"
        var s3Vo = "Save this now and try it in your next reel!"
        var cta = "Comment below with your thoughts!"
        var audioVibe = "Cinematic Upbeat Synthwave"

        raw.lines().forEach { line ->
            val trimmed = line.trim()
            when {
                trimmed.startsWith("TITLE:", ignoreCase = true) -> title = trimmed.substringAfter(":").trim()
                trimmed.startsWith("HOOK:", ignoreCase = true) -> hook = trimmed.substringAfter(":").trim()
                trimmed.startsWith("SCENE_1_ACTION:", ignoreCase = true) -> s1Action = trimmed.substringAfter(":").trim()
                trimmed.startsWith("SCENE_1_TEXT:", ignoreCase = true) -> s1Text = trimmed.substringAfter(":").trim()
                trimmed.startsWith("SCENE_1_VO:", ignoreCase = true) -> s1Vo = trimmed.substringAfter(":").trim()
                trimmed.startsWith("SCENE_2_ACTION:", ignoreCase = true) -> s2Action = trimmed.substringAfter(":").trim()
                trimmed.startsWith("SCENE_2_TEXT:", ignoreCase = true) -> s2Text = trimmed.substringAfter(":").trim()
                trimmed.startsWith("SCENE_2_VO:", ignoreCase = true) -> s2Vo = trimmed.substringAfter(":").trim()
                trimmed.startsWith("SCENE_3_ACTION:", ignoreCase = true) -> s3Action = trimmed.substringAfter(":").trim()
                trimmed.startsWith("SCENE_3_TEXT:", ignoreCase = true) -> s3Text = trimmed.substringAfter(":").trim()
                trimmed.startsWith("SCENE_3_VO:", ignoreCase = true) -> s3Vo = trimmed.substringAfter(":").trim()
                trimmed.startsWith("CTA:", ignoreCase = true) -> cta = trimmed.substringAfter(":").trim()
                trimmed.startsWith("AUDIO_VIBE:", ignoreCase = true) -> audioVibe = trimmed.substringAfter(":").trim()
            }
        }

        return ReelScript(
            id = "script_${System.currentTimeMillis()}",
            title = title,
            estimatedDurationSeconds = duration,
            targetNiche = niche,
            openingHook = hook,
            scenes = listOf(
                ScriptScene(1, "0:00 - 0:03", s1Action, s1Text, s1Vo),
                ScriptScene(2, "0:03 - 0:22", s2Action, s2Text, s2Vo),
                ScriptScene(3, "0:22 - 0:30", s3Action, s3Text, s3Vo)
            ),
            ctaClosing = cta,
            audioRecommendation = audioVibe
        )
    }

    private fun generateFallbackScript(
        topic: String,
        niche: CreatorNiche,
        language: ContentLanguage,
        duration: Int
    ): ReelScript {
        val isHindi = language == ContentLanguage.HINDI || language == ContentLanguage.HINGLISH
        return ReelScript(
            id = "script_fb_${System.currentTimeMillis()}",
            title = if (isHindi) "$topic पर वायरल रील स्क्रिप्ट" else "High-Retention Script on $topic",
            estimatedDurationSeconds = duration,
            targetNiche = niche,
            openingHook = if (isHindi) "ये 1 गलती आपका पूरा रील रीच खत्म कर रही है!" else "This 1 mistake is killing your Reel reach!",
            scenes = listOf(
                ScriptScene(
                    sceneNumber = 1,
                    timeCode = "0:00 - 0:03",
                    visualAction = "Point directly to camera with intense expression; quick zoom in.",
                    onScreenText = if (isHindi) "रुकिए! ये मत करना 🛑" else "WAIT! STOP DOING THIS 🛑",
                    voiceoverOrDialogue = if (isHindi) "अगर आप $topic पर रील बनाते हैं, तो पहले ये सुनो..." else "If you create content around $topic, stop scrolling right now."
                ),
                ScriptScene(
                    sceneNumber = 2,
                    timeCode = "0:03 - 0:22",
                    visualAction = "Show 2 quick b-roll demonstration clips with dynamic subtitles.",
                    onScreenText = if (isHindi) "2 सीक्रेट रूल्स जो काम करते हैं" else "2 Proven Rules That Work",
                    voiceoverOrDialogue = if (isHindi) "पहला: शुरुआती 3 सेकंड में वैल्यू प्रॉमिस करो। दूसरा: वीडियो को ऐसा लूप करो कि लोग दो बार देखें।" else "Rule 1: Deliver on the hook immediately without long intros. Rule 2: Craft a seamless loop back to the start."
                ),
                ScriptScene(
                    sceneNumber = 3,
                    timeCode = "0:22 - 0:30",
                    visualAction = "Hold up phone screen or gesture towards save button.",
                    onScreenText = if (isHindi) "अभी सेव करें 💾" else "SAVE FOR LATER 💾",
                    voiceoverOrDialogue = if (isHindi) "इसे अभी सेव कर लो ताकि रील शूट करते समय काम आए। कमेंट में बताओ आपका अगला टॉपिक क्या है!" else "Save this right now before you forget, and drop your questions in the comments!"
                )
            ),
            ctaClosing = if (isHindi) "शेयर करो अपने क्रिएटर दोस्तों के साथ! 🚀" else "Share with someone who needs to see this! 🚀",
            audioRecommendation = "High-BPM Trending Electronic / Bass Drops"
        )
    }
}
