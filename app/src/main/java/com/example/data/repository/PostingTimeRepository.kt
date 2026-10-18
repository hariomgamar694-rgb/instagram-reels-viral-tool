package com.example.data.repository

import com.example.data.model.CreatorNiche

data class PostingTimeWindow(
    val dayOfWeek: String,
    val peakTimeWindowIST: String,
    val secondaryWindowIST: String,
    val audienceActiveDensity: String, // "High", "Very High", "Moderate"
    val whyItWorks: String
)

data class NichePostingRecommendation(
    val niche: CreatorNiche,
    val bestOverallDays: List<String>,
    val bestSlotEveningIST: String,
    val bestSlotLunchIST: String,
    val bestSlotMorningIST: String,
    val generalAudienceInsights: String,
    val connectionStatusNote: String,
    val dailySchedule: List<PostingTimeWindow>
)

class PostingTimeRepository {

    fun getPostingRecommendations(niche: CreatorNiche): NichePostingRecommendation {
        val schedule = listOf(
            PostingTimeWindow(
                dayOfWeek = "Monday / सोमवार",
                peakTimeWindowIST = "7:30 PM - 9:00 PM IST",
                secondaryWindowIST = "12:45 PM - 1:45 PM IST",
                audienceActiveDensity = "High",
                whyItWorks = "Evening commute and post-dinner unwind after work/college."
            ),
            PostingTimeWindow(
                dayOfWeek = "Tuesday / मंगलवार",
                peakTimeWindowIST = "8:00 PM - 9:30 PM IST",
                secondaryWindowIST = "1:00 PM - 2:00 PM IST",
                audienceActiveDensity = "Very High",
                whyItWorks = "Steady weekday scrolling routine with strong explore tab engagement."
            ),
            PostingTimeWindow(
                dayOfWeek = "Wednesday / बुधवार",
                peakTimeWindowIST = "6:30 PM - 8:30 PM IST",
                secondaryWindowIST = "11:30 AM - 12:30 PM IST",
                audienceActiveDensity = "Very High",
                whyItWorks = "Mid-week engagement peak across metropolitan and tier-2 regions."
            ),
            PostingTimeWindow(
                dayOfWeek = "Thursday / गुरुवार",
                peakTimeWindowIST = "7:00 PM - 9:00 PM IST",
                secondaryWindowIST = "2:00 PM - 3:00 PM IST",
                audienceActiveDensity = "High",
                whyItWorks = "Good momentum for educational tutorials and how-to saves."
            ),
            PostingTimeWindow(
                dayOfWeek = "Friday / शुक्रवार",
                peakTimeWindowIST = "5:00 PM - 8:00 PM IST",
                secondaryWindowIST = "9:30 PM - 11:00 PM IST",
                audienceActiveDensity = "Very High",
                whyItWorks = "Weekend vibe kickoff, high consumption of comedy, lifestyle, and music reels."
            ),
            PostingTimeWindow(
                dayOfWeek = "Saturday / शनिवार",
                peakTimeWindowIST = "11:00 AM - 1:30 PM IST",
                secondaryWindowIST = "7:00 PM - 10:30 PM IST",
                audienceActiveDensity = "Highest Peak",
                whyItWorks = "Late morning relaxed scrolling and late night entertainment binge."
            ),
            PostingTimeWindow(
                dayOfWeek = "Sunday / रविवार",
                peakTimeWindowIST = "10:30 AM - 1:00 PM IST",
                secondaryWindowIST = "6:00 PM - 9:00 PM IST",
                audienceActiveDensity = "Highest Peak",
                whyItWorks = "Full-day leisure consumption; morning devotional & lifestyle, evening humor & vlogs."
            )
        )

        val (bestEvening, bestLunch, bestMorning, insight) = when (niche) {
            CreatorNiche.TECH_AI, CreatorNiche.EDUCATION, CreatorNiche.FINANCE -> Quadruple(
                "7:00 PM - 8:30 PM IST",
                "1:00 PM - 2:00 PM IST",
                "8:30 AM - 9:30 AM IST",
                "Working professionals and students check tech/finance during lunch break and post-work evening hours."
            )
            CreatorNiche.DEVOTIONAL -> Quadruple(
                "6:30 PM - 8:00 PM IST (Sandhya Aarti time)",
                "12:00 PM - 1:00 PM IST",
                "6:00 AM - 8:00 AM IST (Pratah Kaal)",
                "Devotional and spiritual reels perform exceptionally well in early morning hours (6:00 AM - 8:00 AM IST) and twilight evening."
            )
            CreatorNiche.FITNESS -> Quadruple(
                "6:00 PM - 7:30 PM IST (Evening workout slot)",
                "1:30 PM - 2:30 PM IST",
                "6:30 AM - 8:00 AM IST (Morning routine)",
                "Fitness enthusiasts seek inspiration early morning before gym and early evening after work."
            )
            CreatorNiche.FOOD -> Quadruple(
                "7:30 PM - 9:30 PM IST (Dinner craving peak)",
                "12:30 PM - 2:00 PM IST (Lunch hunger)",
                "10:00 AM - 11:30 AM IST",
                "Recipe and food reels trigger maximum saves and shares right before meal times."
            )
            else -> Quadruple(
                "7:30 PM - 9:30 PM IST",
                "1:00 PM - 2:30 PM IST",
                "9:00 AM - 10:30 AM IST",
                "Entertainment and relatable skits enjoy widespread high consumption across evening leisure hours and weekend afternoons."
            )
        }

        return NichePostingRecommendation(
            niche = niche,
            bestOverallDays = listOf("Wednesday", "Friday", "Saturday", "Sunday"),
            bestSlotEveningIST = bestEvening,
            bestSlotLunchIST = bestLunch,
            bestSlotMorningIST = bestMorning,
            generalAudienceInsights = insight,
            connectionStatusNote = "Status: General Aggregated Benchmark. Instagram Insights API is not connected. These recommendations represent standard Indian and Global creator demographic active hours.",
            dailySchedule = schedule
        )
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
