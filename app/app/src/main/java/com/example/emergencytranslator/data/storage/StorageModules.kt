package com.example.emergencytranslator.data.storage

import android.content.Context
import androidx.room.Room
import com.example.emergencytranslator.data.storage.daos.HistoryItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModules {

    @Singleton
    @Provides
    fun providesDataStoreHelper(
        @ApplicationContext context: Context
    ): DataStoreHelper {
        return DataStoreHelper(context = context)
    }

    @Provides
    fun providesHistoryItemDao(database: HistoryDatabase): HistoryItemDao {
        return database.historyItemDao()
    }

    @Provides
    @Singleton
    fun provideHistoryDatabase(@ApplicationContext appContext: Context): HistoryDatabase {
        return Room.databaseBuilder(
            appContext,
            HistoryDatabase::class.java,
            "HistoryDatabase"
        ).build()
    }
}