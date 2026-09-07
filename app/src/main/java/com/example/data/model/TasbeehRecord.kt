package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasbeeh_records")
data class TasbeehRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val phrase: String,
    val count: Int,
    val target: Int,
    val timestamp: Long = System.currentTimeMillis()
)
