package com.livestreaming.streamly.ui.profile.di

import com.livestreaming.streamly.ui.profile.data.repository.ProfileRepositoryImpl
import com.livestreaming.streamly.ui.profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProfileRepoModule {
    @Binds
    @ViewModelScoped
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}
