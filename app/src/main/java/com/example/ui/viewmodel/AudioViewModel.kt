package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AudioItem
import com.example.data.model.ContentLanguage
import com.example.data.model.SavedItemType
import com.example.data.remote.TrendingDataSources
import com.example.data.repository.SavedVaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AudioUiState {
    object Loading : AudioUiState()
    data class Success(val audios: List<AudioItem>) : AudioUiState()
    object Empty : AudioUiState()
    data class Error(val message: String) : AudioUiState()
}

class AudioViewModel(application: Application) : AndroidViewModel(application) {

    private val vaultRepo = SavedVaultRepository(AppDatabase.getInstance(application).savedItemDao())

    private val _uiState = MutableStateFlow<AudioUiState>(AudioUiState.Loading)
    val uiState: StateFlow<AudioUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedLanguage = MutableStateFlow<ContentLanguage?>(null)
    val selectedLanguage: StateFlow<ContentLanguage?> = _selectedLanguage.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val availableCategories = listOf(
        "All",
        "Bollywood / Dramatic",
        "Dance",
        "Bhakti / Devotional",
        "Regional",
        "Comedy / Skit",
        "Tech & Motivation",
        "Aesthetic"
    )

    init {
        loadAudios()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        loadAudios()
    }

    fun setLanguageFilter(lang: ContentLanguage?) {
        _selectedLanguage.value = lang
        loadAudios()
    }

    fun setCategoryFilter(category: String) {
        _selectedCategory.value = category
        loadAudios()
    }

    fun loadAudios() {
        viewModelScope.launch {
            _uiState.value = AudioUiState.Loading
            try {
                val results = TrendingDataSources.getAudios(
                    query = _searchQuery.value,
                    language = _selectedLanguage.value,
                    category = _selectedCategory.value
                )
                if (results.isEmpty()) {
                    _uiState.value = AudioUiState.Empty
                } else {
                    _uiState.value = AudioUiState.Success(results)
                }
            } catch (e: Exception) {
                _uiState.value = AudioUiState.Error(e.localizedMessage ?: "Failed to load audio tracks")
            }
        }
    }

    fun saveAudioToVault(audio: AudioItem, onComplete: () -> Unit) {
        viewModelScope.launch {
            vaultRepo.saveItem(
                type = SavedItemType.AUDIO_FAVORITE,
                title = audio.title,
                content = "Artist: ${audio.artist}\nVibe: ${audio.vibeTag}\nLink: ${audio.originalReelsUrl}",
                metadataJson = audio.originalReelsUrl,
                tagsCsv = audio.category
            )
            onComplete()
        }
    }
}
