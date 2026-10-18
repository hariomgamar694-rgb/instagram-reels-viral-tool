package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_items")
data class SavedItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // CAPTION, SCRIPT, HASHTAG_SET, CONTENT_PLAN, AUDIO_FAVORITE
    val title: String,
    val content: String,
    val metadataJson: String = "",
    val language: String = "hi",
    val tagsCsv: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reel_analytics")
data class AnalyticsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reelTitle: String,
    val nicheName: String,
    val views: Int,
    val watchTimePercentage: Float,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val saves: Int,
    val coachScore: Int,
    val adviceText: String,
    val loggedAt: Long = System.currentTimeMillis()
)
