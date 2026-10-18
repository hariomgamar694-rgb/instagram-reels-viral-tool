package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CreatorNiche
import com.example.data.repository.AnalyticsRepository
import com.example.data.repository.CoachAuditResult
import com.example.data.repository.GrowthCoachRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GrowthCoachViewModel(application: Application) : AndroidViewModel(application) {

    private val coachRepo = GrowthCoachRepository()
    private val analyticsRepo = AnalyticsRepository(AppDatabase.getInstance(application).analyticsDao())

    val reelTitle = MutableStateFlow("My Latest Tech Reel")
    val selectedNiche = MutableStateFlow(CreatorNiche.TECH_AI)
    val contentGoal = MutableStateFlow("Reach & New Followers")

    // Metrics inputs
    val viewsInput = MutableStateFlow("12500")
    val watchTimeInput = MutableStateFlow("68") // percentage
    val likesInput = MutableStateFlow("940")
    val commentsInput = MutableStateFlow("42")
    val sharesInput = MutableStateFlow("195")
    val savesInput = MutableStateFlow("165")

    private val _auditResult = MutableStateFlow<CoachAuditResult?>(null)
    val auditResult: StateFlow<CoachAuditResult?> = _auditResult.asStateFlow()

    private val _isSavedToTracker = MutableStateFlow(false)
    val isSavedToTracker: StateFlow<Boolean> = _isSavedToTracker.asStateFlow()

    init {
        runAudit()
    }

    fun runAudit() {
        val views = viewsInput.value.toIntOrNull() ?: 1
        val watchTime = watchTimeInput.value.toFloatOrNull() ?: 50f
        val likes = likesInput.value.toIntOrNull() ?: 0
        val comments = commentsInput.value.toIntOrNull() ?: 0
        val shares = sharesInput.value.toIntOrNull() ?: 0
        val saves = savesInput.value.toIntOrNull() ?: 0

        _auditResult.value = coachRepo.analyzeReelPerformance(
            niche = selectedNiche.value,
            goal = contentGoal.value,
            views = views,
            watchTimePercentage = watchTime,
            likes = likes,
            comments = comments,
            shares = shares,
            saves = saves
        )
        _isSavedToTracker.value = false
    }

    fun saveToAnalyticsTracker(onComplete: () -> Unit) {
        val audit = _auditResult.value ?: return
        val views = viewsInput.value.toIntOrNull() ?: 1
        val watchTime = watchTimeInput.value.toFloatOrNull() ?: 50f
        val likes = likesInput.value.toIntOrNull() ?: 0
        val comments = commentsInput.value.toIntOrNull() ?: 0
        val shares = sharesInput.value.toIntOrNull() ?: 0
        val saves = savesInput.value.toIntOrNull() ?: 0

        viewModelScope.launch {
            analyticsRepo.logReelPerformance(
                reelTitle = reelTitle.value,
                niche = selectedNiche.value,
                views = views,
                watchTimePercentage = watchTime,
                likes = likes,
                comments = comments,
                shares = shares,
                saves = saves,
                coachScore = audit.overallHealthScore,
                adviceText = audit.hookRating + " | " + audit.prioritizedActionSteps.firstOrNull().orEmpty()
            )
            _isSavedToTracker.value = true
            onComplete()
        }
    }
}
