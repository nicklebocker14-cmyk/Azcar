package com.example.data.repository

import com.example.data.local.CustomDhikrDao
import com.example.data.local.FavoriteDao
import com.example.data.local.TasbeehDao
import com.example.data.model.CustomDhikr
import com.example.data.model.FavoriteItem
import com.example.data.model.TasbeehRecord
import kotlinx.coroutines.flow.Flow

class DhikrRepository(
    private val customDhikrDao: CustomDhikrDao,
    private val favoriteDao: FavoriteDao,
    private val tasbeehDao: TasbeehDao
) {
    val allCustomAdhkar: Flow<List<CustomDhikr>> = customDhikrDao.getAllCustomAdhkar()
    val allFavorites: Flow<List<FavoriteItem>> = favoriteDao.getAllFavorites()
    val recentTasbeehRecords: Flow<List<TasbeehRecord>> = tasbeehDao.getRecentRecords()

    fun isFavoriteFlow(key: String): Flow<Boolean> = favoriteDao.isFavoriteFlow(key)

    suspend fun isFavorite(key: String): Boolean = favoriteDao.isFavorite(key)

    suspend fun toggleFavorite(
        key: String,
        title: String,
        content: String,
        category: String,
        targetCount: Int,
        source: String
    ): Boolean {
        return if (favoriteDao.isFavorite(key)) {
            favoriteDao.deleteByKey(key)
            false
        } else {
            favoriteDao.insert(
                FavoriteItem(
                    dhikrKey = key,
                    title = title,
                    content = content,
                    category = category,
                    targetCount = targetCount,
                    source = source
                )
            )
            true
        }
    }

    suspend fun removeFavorite(key: String) {
        favoriteDao.deleteByKey(key)
    }

    suspend fun insertCustomDhikr(dhikr: CustomDhikr): Long {
        return customDhikrDao.insert(dhikr)
    }

    suspend fun updateCustomDhikr(dhikr: CustomDhikr) {
        customDhikrDao.update(dhikr)
    }

    suspend fun deleteCustomDhikr(dhikr: CustomDhikr) {
        customDhikrDao.delete(dhikr)
        favoriteDao.deleteByKey("custom_${dhikr.id}")
    }

    suspend fun recordTasbeehSession(phrase: String, count: Int, target: Int) {
        if (count > 0) {
            tasbeehDao.insert(
                TasbeehRecord(
                    phrase = phrase,
                    count = count,
                    target = target
                )
            )
        }
    }

    suspend fun clearTasbeehHistory() {
        tasbeehDao.clearAll()
    }
}
