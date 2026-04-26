package com.livestreaming.streamly.ui.setup.di

import com.livestreaming.streamly.ui.setup.data.repository.SetupRepositoryImpl
import com.livestreaming.streamly.ui.setup.domain.repository.SetupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class SetupRepoModule {
    @Binds
    @ViewModelScoped
    abstract fun bindSetupRepository(impl: SetupRepositoryImpl): SetupRepository
}