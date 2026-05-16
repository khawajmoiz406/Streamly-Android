package com.livestreaming.streamly.ui.watch.di

import com.livestreaming.streamly.ui.watch.data.repository.WatchRepositoryImpl
import com.livestreaming.streamly.ui.watch.domain.repository.WatchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WatchRepoModule {
    @Binds
    @Singleton
    abstract fun bindWatchRepository(impl: WatchRepositoryImpl): WatchRepository
}