package com.livestreaming.streamly.ui.auth.di

import com.livestreaming.streamly.ui.auth.data.repository.AuthRepositoryImpl
import com.livestreaming.streamly.ui.auth.domain.repository.LoginRepository
import com.livestreaming.streamly.ui.auth.domain.repository.RegisterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthRepoModule {
    @Binds
    @ViewModelScoped
    abstract fun bindLoginRepository(impl: AuthRepositoryImpl): LoginRepository

    @Binds
    @ViewModelScoped
    abstract fun bindRegisterRepository(impl: AuthRepositoryImpl): RegisterRepository
}