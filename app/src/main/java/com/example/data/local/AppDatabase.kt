package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDao {
    @Query("SELECT * FROM saved_items ORDER BY createdAt DESC")
    fun getAllSavedItems(): Flow<List<SavedItemEntity>>

    @Query("SELECT * FROM saved_items WHERE type = :type ORDER BY createdAt DESC")
    fun getSavedItemsByType(type: String): Flow<List<SavedItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: SavedItemEntity): Long

    @Delete
    suspend fun deleteItem(item: SavedItemEntity)

    @Query("DELETE FROM saved_items WHERE id = :id")
    suspend fun deleteItemById(id: Long)

    @Query("SELECT COUNT(*) FROM saved_items")
    fun getSavedItemsCount(): Flow<Int>
}

@Dao
interface AnalyticsDao {
    @Query("SELECT * FROM reel_analytics ORDER BY loggedAt DESC")
    fun getAllAnalytics(): Flow<List<AnalyticsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalytics(analytics: AnalyticsEntity): Long

    @Query("DELETE FROM reel_analytics WHERE id = :id")
    suspend fun deleteAnalyticsById(id: Long)

    @Query("SELECT AVG(views) FROM reel_analytics")
    suspend fun getAverageViews(): Double?

    @Query("SELECT AVG(watchTimePercentage) FROM reel_analytics")
    suspend fun getAverageWatchTime(): Double?
}

@Database(
    entities = [SavedItemEntity::class, AnalyticsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedItemDao(): SavedItemDao
    abstract fun analyticsDao(): AnalyticsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hr_viral_ai.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
