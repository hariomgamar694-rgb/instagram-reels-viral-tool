package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AnalyticsEntity
import com.example.data.local.AppDatabase
import com.example.data.repository.AnalyticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnalyticsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AnalyticsRepository(AppDatabase.getInstance(application).analyticsDao())

    val allReels: StateFlow<List<AnalyticsEntity>> = repo.allAnalytics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _averageViews = MutableStateFlow(0.0)
    val averageViews: StateFlow<Double> = _averageViews.asStateFlow()

    private val _averageWatchTime = MutableStateFlow(0.0)
    val averageWatchTime: StateFlow<Double> = _averageWatchTime.asStateFlow()

    init {
        refreshStats()
    }

    fun refreshStats() {
        viewModelScope.launch {
            _averageViews.value = repo.getAverageViews()
            _averageWatchTime.value = repo.getAverageWatchTime()
        }
    }

    fun deleteEntry(id: Long) {
        viewModelScope.launch {
            repo.deleteAnalytics(id)
            refreshStats()
        }
    }
}
