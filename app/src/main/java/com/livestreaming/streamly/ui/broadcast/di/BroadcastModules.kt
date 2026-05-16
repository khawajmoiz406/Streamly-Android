package com.livestreaming.streamly.ui.broadcast.di

import com.livestreaming.streamly.ui.broadcast.data.repository.BroadcastRepositoryImpl
import com.livestreaming.streamly.ui.broadcast.domain.repository.BroadcastRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BroadcastRepoModule {
    @Binds
    @Singleton
    abstract fun bindBroadcastRepository(impl: BroadcastRepositoryImpl): BroadcastRepository
}