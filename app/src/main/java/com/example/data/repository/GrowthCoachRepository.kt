package com.example.data.repository

import com.example.data.model.CreatorNiche

data class CoachAuditResult(
    val overallHealthScore: Int, // 0-100
    val hookRating: String, // Strong, Moderate, Needs Work
    val hookExplanation: String,
    val shareToViewRatioPercent: Float,
    val saveToViewRatioPercent: Float,
    val algorithmSignalsEvaluation: String,
    val prioritizedActionSteps: List<String>,
    val honestAlgorithmNotice: String
)

class GrowthCoachRepository {

    fun analyzeReelPerformance(
        niche: CreatorNiche,
        goal: String,
        views: Int,
        watchTimePercentage: Float,
        likes: Int,
        comments: Int,
        shares: Int,
        saves: Int
    ): CoachAuditResult {
        val safeViews = if (views <= 0) 1 else views

        val shareRatio = (shares.toFloat() / safeViews.toFloat()) * 100f
        val saveRatio = (saves.toFloat() / safeViews.toFloat()) * 100f
        val likeRatio = (likes.toFloat() / safeViews.toFloat()) * 100f
        val commentRatio = (comments.toFloat() / safeViews.toFloat()) * 100f

        // Evaluated based on real Instagram recommendation signals
        // Note: Algorithm weighs Watch Time + Shares + Saves much higher than simple Likes!
        var score = 50

        // Watch time scoring
        if (watchTimePercentage >= 85f) score += 25
        else if (watchTimePercentage >= 65f) score += 15
        else if (watchTimePercentage >= 45f) score += 5
        else score -= 15

        // Share ratio scoring (Top viral factor)
        if (shareRatio >= 3.0f) score += 15
        else if (shareRatio >= 1.0f) score += 8
        else if (shareRatio < 0.3f) score -= 5

        // Save ratio scoring (High value benchmark)
        if (saveRatio >= 2.5f) score += 10
        else if (saveRatio >= 0.8f) score += 5

        val finalScore = score.coerceIn(15, 98)

        val (hookRating, hookExplanation) = when {
            watchTimePercentage >= 75f -> Pair(
                "Strong Opening Hook 🎯",
                "Viewers stay past the crucial 3-second mark. Your pattern interrupt is effective."
            )
            watchTimePercentage >= 50f -> Pair(
                "Average Retention (Drop-off detected)",
                "About half of viewers leave before completion. Trim the first 2 seconds and eliminate slow intro phrases like 'Hello guys'."
            )
            else -> Pair(
                "High Early Drop-Off ⚠️",
                "Most users are scrolling away in the first 1-3 seconds. Start in media res (right in the middle of action or tension) with large contrasting text."
            )
        }

        val actionSteps = mutableListOf<String>()

        if (shareRatio < 1.0f) {
            actionSteps.add("Boost Shareability: Ask yourself 'Would someone send this to a specific friend or group chat?' Make your punchline or tip relatable to a specific person.")
        } else {
            actionSteps.add("Great Share Momentum: Your content resonates with interpersonal recommendations.")
        }

        if (saveRatio < 0.8f) {
            actionSteps.add("Increase Save Rate: Use checklist formats, step-by-step guides, or resource lists that viewers cannot digest in one viewing and must bookmark.")
        } else {
            actionSteps.add("High Educational Value: High save rates show viewers find your Reel reference-worthy.")
        }

        if (commentRatio < 0.2f) {
            actionSteps.add("Stimulate Comment Conversations: End with an open debate or ask for a specific recommendation rather than a generic 'comment below'.")
        }

        actionSteps.add("Visual Pacing: Change camera angle or visual element every 2.5 - 3.5 seconds to reset viewer attention.")

        val signalsEval = """
            • Watch Time: ${String.format("%.1f", watchTimePercentage)}%
            • Share-to-View: ${String.format("%.2f", shareRatio)}% (Benchmark for viral explore: >1.5%)
            • Save-to-View: ${String.format("%.2f", saveRatio)}% (Benchmark for value reels: >1.0%)
            • Like-to-View: ${String.format("%.2f", likeRatio)}%
        """.trimIndent()

        val notice = "Notice: The Instagram Explore and Reels algorithm evaluates personalized viewer interest, watch time, and share rate. No score guarantees virality."

        return CoachAuditResult(
            overallHealthScore = finalScore,
            hookRating = hookRating,
            hookExplanation = hookExplanation,
            shareToViewRatioPercent = shareRatio,
            saveToViewRatioPercent = saveRatio,
            algorithmSignalsEvaluation = signalsEval,
            prioritizedActionSteps = actionSteps,
            honestAlgorithmNotice = notice
        )
    }
}
