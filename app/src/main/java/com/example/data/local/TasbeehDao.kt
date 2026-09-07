package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.TasbeehRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface TasbeehDao {
    @Query("SELECT * FROM tasbeeh_records ORDER BY timestamp DESC LIMIT 50")
    fun getRecentRecords(): Flow<List<TasbeehRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: TasbeehRecord): Long

    @Query("DELETE FROM tasbeeh_records")
    suspend fun clearAll()
}
