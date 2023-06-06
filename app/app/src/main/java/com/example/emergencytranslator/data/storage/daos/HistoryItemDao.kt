package com.example.emergencytranslator.data.storage.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.emergencytranslator.data.storage.entities.HistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryItemDao {
    companion object{
        const val TABLE_NAME = "history_item"
    }
    @Insert
    suspend fun insert(item: HistoryItem)

    @Delete
    suspend fun delete(item: HistoryItem)

    @Query("SELECT * from $TABLE_NAME ORDER BY timestamp DESC")
    fun getAllItems(): Flow<List<HistoryItem>>

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAllItems()
}