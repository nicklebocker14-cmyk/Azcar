package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dhikrKey: String, // unique key matching built-in ID or custom_ID
    val title: String,
    val content: String,
    val category: String,
    val targetCount: Int,
    val source: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
