package com.app.sampleapp.di

import android.content.Context
import com.app.sampleapp.data.local.preference.PreferenceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePreferenceProvider(
        @ApplicationContext context: Context
    ): PreferenceProvider = PreferenceProvider(context)

}