package com.example.emergencytranslator.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.emergencytranslator.data.storage.daos.HistoryItemDao
import com.example.emergencytranslator.data.storage.entities.HistoryItem

@Database(entities = [HistoryItem::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyItemDao(): HistoryItemDao
}