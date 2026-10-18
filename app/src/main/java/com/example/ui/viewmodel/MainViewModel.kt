package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ContentLanguage
import com.example.data.model.CreatorNiche
import com.example.data.model.TrendItem
import com.example.data.model.UserProfile
import com.example.data.remote.GeminiClient
import com.example.data.remote.TrendingDataSources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)

    private val _currentLanguage = MutableStateFlow(ContentLanguage.HINDI)
    val currentLanguage: StateFlow<ContentLanguage> = _currentLanguage.asStateFlow()

    private val _selectedNiche = MutableStateFlow(CreatorNiche.ENTERTAINMENT)
    val selectedNiche: StateFlow<CreatorNiche> = _selectedNiche.asStateFlow()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            name = "Creator",
            instagramHandle = "@creator_india",
            primaryNiche = CreatorNiche.ENTERTAINMENT,
            preferredLanguage = ContentLanguage.HINDI,
            hasApiKeyConfigured = GeminiClient.isApiKeyConfigured()
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _trends = MutableStateFlow<List<TrendItem>>(emptyList())
    val trends: StateFlow<List<TrendItem>> = _trends.asStateFlow()

    private val _isTrendsLoading = MutableStateFlow(false)
    val isTrendsLoading: StateFlow<Boolean> = _isTrendsLoading.asStateFlow()

    val totalSavedCount: StateFlow<Int> = db.savedItemDao().getSavedItemsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        loadTrends()
    }

    fun setLanguage(language: ContentLanguage) {
        _currentLanguage.value = language
        _userProfile.value = _userProfile.value.copy(preferredLanguage = language)
    }

    fun setNiche(niche: CreatorNiche) {
        _selectedNiche.value = niche
        _userProfile.value = _userProfile.value.copy(primaryNiche = niche)
        loadTrends()
    }

    fun updateProfile(name: String, handle: String, niche: CreatorNiche) {
        _userProfile.value = _userProfile.value.copy(
            name = name,
            instagramHandle = handle,
            primaryNiche = niche
        )
        _selectedNiche.value = niche
    }

    fun loadTrends() {
        viewModelScope.launch {
            _isTrendsLoading.value = true
            try {
                val data = TrendingDataSources.getTrends(_selectedNiche.value)
                _trends.value = data
            } catch (e: Exception) {
                _trends.value = emptyList()
            } finally {
                _isTrendsLoading.value = false
            }
        }
    }
}
