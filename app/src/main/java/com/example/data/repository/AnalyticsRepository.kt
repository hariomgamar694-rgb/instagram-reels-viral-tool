package com.example.data.repository

import com.example.data.local.AnalyticsDao
import com.example.data.local.AnalyticsEntity
import com.example.data.model.CreatorNiche
import kotlinx.coroutines.flow.Flow

class AnalyticsRepository(private val dao: AnalyticsDao) {

    val allAnalytics: Flow<List<AnalyticsEntity>> = dao.getAllAnalytics()

    suspend fun logReelPerformance(
        reelTitle: String,
        niche: CreatorNiche,
        views: Int,
        watchTimePercentage: Float,
        likes: Int,
        comments: Int,
        shares: Int,
        saves: Int,
        coachScore: Int,
        adviceText: String
    ): Long {
        val entity = AnalyticsEntity(
            reelTitle = reelTitle.ifBlank { "Untitled Reel" },
            nicheName = niche.name,
            views = views,
            watchTimePercentage = watchTimePercentage,
            likes = likes,
            comments = comments,
            shares = shares,
            saves = saves,
            coachScore = coachScore,
            adviceText = adviceText,
            loggedAt = System.currentTimeMillis()
        )
        return dao.insertAnalytics(entity)
    }

    suspend fun deleteAnalytics(id: Long) {
        dao.deleteAnalyticsById(id)
    }

    suspend fun getAverageViews(): Double {
        return dao.getAverageViews() ?: 0.0
    }

    suspend fun getAverageWatchTime(): Double {
        return dao.getAverageWatchTime() ?: 0.0
    }
}
