package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CustomDhikr
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomDhikrDao {
    @Query("SELECT * FROM custom_adhkar ORDER BY createdAt DESC")
    fun getAllCustomAdhkar(): Flow<List<CustomDhikr>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dhikr: CustomDhikr): Long

    @Update
    suspend fun update(dhikr: CustomDhikr)

    @Delete
    suspend fun delete(dhikr: CustomDhikr)

    @Query("DELETE FROM custom_adhkar WHERE id = :id")
    suspend fun deleteById(id: Long)
}
