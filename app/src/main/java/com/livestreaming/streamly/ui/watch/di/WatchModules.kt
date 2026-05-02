package com.livestreaming.streamly.ui.watch.di

import com.livestreaming.streamly.ui.watch.data.repository.WatchRepositoryImpl
import com.livestreaming.streamly.ui.watch.domain.repository.WatchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class WatchRepoModule {
    @Binds
    @ViewModelScoped
    abstract fun bindWatchRepository(impl: WatchRepositoryImpl): WatchRepository
}