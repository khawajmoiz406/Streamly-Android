package com.livestreaming.streamly.ui.home.di

import com.livestreaming.streamly.ui.home.data.repository.HomeRepositoryImpl
import com.livestreaming.streamly.ui.home.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeRepoModule {
    @Binds
    @ViewModelScoped
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository
}