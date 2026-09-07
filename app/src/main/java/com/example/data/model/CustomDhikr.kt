package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_adhkar")
data class CustomDhikr(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val targetCount: Int = 33,
    val category: String = "أذكار مخصصة",
    val iconName: String = "sparkles",
    val createdAt: Long = System.currentTimeMillis()
)
