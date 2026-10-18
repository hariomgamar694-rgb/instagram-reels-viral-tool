package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CaptionStyle
import com.example.data.model.ContentLanguage
import com.example.data.model.CreatorNiche
import com.example.data.model.HashtagGroup
import com.example.data.model.SavedItemType
import com.example.data.repository.HashtagRepository
import com.example.data.repository.SavedVaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HashtagViewModel(application: Application) : AndroidViewModel(application) {

    private val hashtagRepo = HashtagRepository()
    private val vaultRepo = SavedVaultRepository(AppDatabase.getInstance(application).savedItemDao())

    val topic = MutableStateFlow("Viral Reels Growth Tips")
    val selectedNiche = MutableStateFlow(CreatorNiche.TECH_AI)
    val selectedLanguage = MutableStateFlow(ContentLanguage.HINDI)
    val targetAudience = MutableStateFlow("Indian Creators & Gen Z")
    val selectedStyle = MutableStateFlow(CaptionStyle.SHORT_PUNCHY)

    private val _hashtagGroup = MutableStateFlow<HashtagGroup?>(null)
    val hashtagGroup: StateFlow<HashtagGroup?> = _hashtagGroup.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedTags = MutableStateFlow<Set<String>>(emptySet())
    val selectedTags: StateFlow<Set<String>> = _selectedTags.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    init {
        generateHashtags()
    }

    fun toggleTagSelection(tag: String) {
        val current = _selectedTags.value.toMutableSet()
        if (current.contains(tag)) {
            current.remove(tag)
        } else {
            current.add(tag)
        }
        _selectedTags.value = current
    }

    fun selectAllTags() {
        val group = _hashtagGroup.value ?: return
        val all = mutableSetOf<String>()
        group.broadTopicTags.forEach { all.add(it.tag) }
        group.specificNicheTags.forEach { all.add(it.tag) }
        group.communityTags.forEach { all.add(it.tag) }
        group.regionalLanguageTags.forEach { all.add(it.tag) }
        _selectedTags.value = all
    }

    fun clearTagSelection() {
        _selectedTags.value = emptySet()
    }

    fun generateHashtags() {
        viewModelScope.launch {
            _isLoading.value = true
            _statusMessage.value = null
            try {
                val result = hashtagRepo.generateHashtags(
                    topic = topic.value,
                    niche = selectedNiche.value,
                    language = selectedLanguage.value,
                    audience = targetAudience.value,
                    style = selectedStyle.value
                )
                _hashtagGroup.value = result
                _selectedTags.value = emptySet()
            } catch (e: Exception) {
                _statusMessage.value = "Error generating hashtags: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAllTagsString(): String {
        val group = _hashtagGroup.value ?: return ""
        val all = mutableListOf<String>()
        group.broadTopicTags.forEach { all.add(it.tag) }
        group.specificNicheTags.forEach { all.add(it.tag) }
        group.communityTags.forEach { all.add(it.tag) }
        group.regionalLanguageTags.forEach { all.add(it.tag) }
        return all.joinToString(" ")
    }

    fun getSelectedTagsString(): String {
        return _selectedTags.value.joinToString(" ")
    }

    fun saveToVault(onComplete: () -> Unit) {
        val group = _hashtagGroup.value ?: return
        val tags = if (_selectedTags.value.isNotEmpty()) getSelectedTagsString() else getAllTagsString()

        viewModelScope.launch {
            vaultRepo.saveItem(
                type = SavedItemType.HASHTAG_SET,
                title = "Hashtags: ${topic.value.take(30)}",
                content = tags,
                tagsCsv = selectedNiche.value.name,
                language = selectedLanguage.value.code
            )
            _statusMessage.value = "Saved to Vault! 💾"
            onComplete()
        }
    }
}
