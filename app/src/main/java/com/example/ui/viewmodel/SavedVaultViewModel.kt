package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.SavedItemEntity
import com.example.data.model.SavedItemType
import com.example.data.repository.SavedVaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavedVaultViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SavedVaultRepository(AppDatabase.getInstance(application).savedItemDao())

    val allSavedItems: StateFlow<List<SavedItemEntity>> = repo.allSavedItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedFilterType = MutableStateFlow<SavedItemType?>(null)

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    fun setFilterType(type: SavedItemType?) {
        selectedFilterType.value = type
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            repo.deleteItem(id)
            _statusMessage.value = "Item removed from Vault"
        }
    }
}
