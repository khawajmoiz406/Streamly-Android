package com.livestreaming.streamly.ui.broadcast.di

import com.livestreaming.streamly.ui.broadcast.data.repository.BroadcastRepositoryImpl
import com.livestreaming.streamly.ui.broadcast.domain.repository.BroadcastRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class BroadcastRepoModule {
    @Binds
    @ViewModelScoped
    abstract fun bindBroadcastRepository(impl: BroadcastRepositoryImpl): BroadcastRepository
}