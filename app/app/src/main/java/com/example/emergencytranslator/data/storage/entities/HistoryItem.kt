package com.example.emergencytranslator.data.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.emergencytranslator.data.storage.daos.HistoryItemDao

@Entity(tableName = HistoryItemDao.TABLE_NAME)
data class HistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val sourceLanguage: String,
    val targetLanguage: String,
    val sourceText: String,
    val targetText: String,
    val timestamp: Long
)
