package com.livestreaming.streamly.core.di

import android.content.Context
import com.livestreaming.streamly.core.pref.EncryptedSharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun createEncryptedSharedPref(@ApplicationContext context: Context): EncryptedSharedPref {
        return EncryptedSharedPref.getInstance(context)
    }
}