package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.model.CreatorNiche
import com.example.data.repository.NichePostingRecommendation
import com.example.data.repository.PostingTimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PostingTimeViewModel : ViewModel() {

    private val postingRepo = PostingTimeRepository()

    val selectedNiche = MutableStateFlow(CreatorNiche.ENTERTAINMENT)

    private val _recommendation = MutableStateFlow(postingRepo.getPostingRecommendations(CreatorNiche.ENTERTAINMENT))
    val recommendation: StateFlow<NichePostingRecommendation> = _recommendation.asStateFlow()

    fun setNiche(niche: CreatorNiche) {
        selectedNiche.value = niche
        _recommendation.value = postingRepo.getPostingRecommendations(niche)
    }
}
