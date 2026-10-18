package com.example.data.repository

import com.example.data.local.SavedItemDao
import com.example.data.local.SavedItemEntity
import com.example.data.model.SavedItemType
import kotlinx.coroutines.flow.Flow

class SavedVaultRepository(private val dao: SavedItemDao) {

    val allSavedItems: Flow<List<SavedItemEntity>> = dao.getAllSavedItems()

    fun getItemsByType(type: SavedItemType): Flow<List<SavedItemEntity>> {
        return dao.getSavedItemsByType(type.name)
    }

    suspend fun saveItem(
        type: SavedItemType,
        title: String,
        content: String,
        metadataJson: String = "",
        language: String = "hi",
        tagsCsv: String = ""
    ): Long {
        val entity = SavedItemEntity(
            type = type.name,
            title = title,
            content = content,
            metadataJson = metadataJson,
            language = language,
            tagsCsv = tagsCsv,
            createdAt = System.currentTimeMillis()
        )
        return dao.insertItem(entity)
    }

    suspend fun deleteItem(id: Long) {
        dao.deleteItemById(id)
    }

    val totalSavedCount: Flow<Int> = dao.getSavedItemsCount()
}
