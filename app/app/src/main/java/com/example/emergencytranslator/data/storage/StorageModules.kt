package com.example.emergencytranslator.data.storage

import android.content.Context
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
}