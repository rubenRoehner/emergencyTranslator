package com.example.emergencytranslator.data.core

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModules {

    @Singleton
    @Provides
    fun providesMLTranslator(): MLTranslator {
        return MLTranslator()
    }

    @Singleton
    @Provides
    fun providesVoiceToTextRecognizer(
        application: Application
    ): VoiceToTextRecognizer {
        return VoiceToTextRecognizer(application = application)
    }

    @Singleton
    @Provides
    fun providesTextToSpeechModule(
        @ApplicationContext context: Context
    ): TTSModule {
        return TTSModule(context = context)
    }
}