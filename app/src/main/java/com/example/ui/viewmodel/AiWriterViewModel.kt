package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CaptionStyle
import com.example.data.model.ContentLanguage
import com.example.data.model.ContentPlan
import com.example.data.model.CreatorNiche
import com.example.data.model.GeneratedCaption
import com.example.data.model.ReelScript
import com.example.data.model.SavedItemType
import com.example.data.repository.CaptionScriptRepository
import com.example.data.repository.SavedVaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class WriterTab {
    CAPTIONS_AND_HOOKS,
    REEL_SCRIPTS,
    CONTENT_CALENDAR
}

class AiWriterViewModel(application: Application) : AndroidViewModel(application) {

    private val writerRepo = CaptionScriptRepository()
    private val vaultRepo = SavedVaultRepository(AppDatabase.getInstance(application).savedItemDao())

    val activeTab = MutableStateFlow(WriterTab.CAPTIONS_AND_HOOKS)

    val topic = MutableStateFlow("3 Editing Mistakes Killing Your Reach")
    val selectedNiche = MutableStateFlow(CreatorNiche.TECH_AI)
    val selectedLanguage = MutableStateFlow(ContentLanguage.HINDI)
    val selectedStyle = MutableStateFlow(CaptionStyle.SHORT_PUNCHY)
    val scriptDuration = MutableStateFlow(30)

    private val _generatedCaption = MutableStateFlow<GeneratedCaption?>(null)
    val generatedCaption: StateFlow<GeneratedCaption?> = _generatedCaption.asStateFlow()

    private val _generatedScript = MutableStateFlow<ReelScript?>(null)
    val generatedScript: StateFlow<ReelScript?> = _generatedScript.asStateFlow()

    private val _contentCalendar = MutableStateFlow<List<ContentPlan>>(emptyList())
    val contentCalendar: StateFlow<List<ContentPlan>> = _contentCalendar.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _statusNotice = MutableStateFlow<String?>(null)
    val statusNotice: StateFlow<String?> = _statusNotice.asStateFlow()

    init {
        generateCaption()
    }

    fun setTab(tab: WriterTab) {
        activeTab.value = tab
        if (tab == WriterTab.REEL_SCRIPTS && _generatedScript.value == null) {
            generateScript()
        } else if (tab == WriterTab.CONTENT_CALENDAR && _contentCalendar.value.isEmpty()) {
            generateCalendar()
        }
    }

    fun generateCaption() {
        viewModelScope.launch {
            _isLoading.value = true
            _statusNotice.value = null
            try {
                val res = writerRepo.generateCaption(
                    topic = topic.value,
                    niche = selectedNiche.value,
                    language = selectedLanguage.value,
                    style = selectedStyle.value
                )
                if (res.isSuccess) {
                    _generatedCaption.value = res.getOrThrow()
                } else {
                    _statusNotice.value = "AI generation error: ${res.exceptionOrNull()?.message}"
                }
            } catch (e: Exception) {
                _statusNotice.value = "Failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun modifyCaption(action: String) {
        val current = _generatedCaption.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val res = writerRepo.modifyCaption(current, action)
                if (res.isSuccess) {
                    _generatedCaption.value = res.getOrThrow()
                }
            } catch (e: Exception) {
                _statusNotice.value = "Could not modify: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun generateScript() {
        viewModelScope.launch {
            _isLoading.value = true
            _statusNotice.value = null
            try {
                val res = writerRepo.generateScript(
                    topic = topic.value,
                    niche = selectedNiche.value,
                    language = selectedLanguage.value,
                    durationSeconds = scriptDuration.value
                )
                if (res.isSuccess) {
                    _generatedScript.value = res.getOrThrow()
                }
            } catch (e: Exception) {
                _statusNotice.value = "Failed to generate script: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun generateCalendar() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = writerRepo.generateContentCalendar(selectedNiche.value, "Viral Reach")
                _contentCalendar.value = list
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveCaptionToVault(onComplete: () -> Unit) {
        val cap = _generatedCaption.value ?: return
        viewModelScope.launch {
            val content = buildString {
                append("🪝 HOOK:\n${cap.firstThreeSecondsHook}\n\n")
                append("📝 CAPTION:\n${cap.captionText}\n\n")
                append("🎯 CTA:\n${cap.callToAction}\n\n")
                append("🏷️ TAGS:\n${cap.hashtags.joinToString(" ")}")
            }
            vaultRepo.saveItem(
                type = SavedItemType.CAPTION,
                title = "Caption: ${cap.firstThreeSecondsHook.take(30)}",
                content = content,
                language = cap.language.code,
                tagsCsv = cap.style.name
            )
            onComplete()
        }
    }

    fun saveScriptToVault(onComplete: () -> Unit) {
        val script = _generatedScript.value ?: return
        viewModelScope.launch {
            val content = buildString {
                append("🎬 TITLE: ${script.title}\n")
                append("⏱️ DURATION: ${script.estimatedDurationSeconds}s\n")
                append("🎵 AUDIO: ${script.audioRecommendation}\n\n")
                script.scenes.forEach { scene ->
                    append("--- SCENE ${scene.sceneNumber} (${scene.timeCode}) ---\n")
                    append("Visual: ${scene.visualAction}\n")
                    append("Text: ${scene.onScreenText}\n")
                    append("VO/Dialogue: ${scene.voiceoverOrDialogue}\n\n")
                }
                append("🎯 CLOSING CTA: ${script.ctaClosing}")
            }
            vaultRepo.saveItem(
                type = SavedItemType.SCRIPT,
                title = script.title,
                content = content,
                tagsCsv = script.targetNiche.name
            )
            onComplete()
        }
    }
}
