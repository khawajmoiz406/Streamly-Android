package com.livestreaming.streamly.ui.profile.presentation

import androidx.lifecycle.viewModelScope
import com.livestreaming.streamly.base.BaseViewModel
import com.livestreaming.streamly.config.theme.ThemeMode
import com.livestreaming.streamly.config.theme.ThemeState
import com.livestreaming.streamly.core.model.ProfileData
import com.livestreaming.streamly.ui.profile.domain.usecase.ChangeThemeModeUseCase
import com.livestreaming.streamly.ui.profile.domain.usecase.ObserveProfileDataUseCase
import com.livestreaming.streamly.ui.profile.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val changeThemeModeUseCase: ChangeThemeModeUseCase,
    private val observeProfileDataUseCase: ObserveProfileDataUseCase,
) : BaseViewModel<ProfileUiState, ProfileEvents>(ProfileUiState()) {
    private val _profileData = MutableStateFlow<ProfileData?>(null)
    val profileData: StateFlow<ProfileData?> = _profileData.asStateFlow()

    fun observeProfile(userId: String) = viewModelScope.launch {
        updateUiState(uiState.value.copy(isLoading = true))
        observeProfileDataUseCase.invoke(userId)
            .catch { ex ->
                val errorStr = ex.toErrorString()
                updateUiState(uiState.value.copy(isLoading = false))
                events.emit(ProfileEvents.OnError(errorStr))
            }
            .collect { data ->
                _profileData.update { data }
                if (uiState.value.isLoading) {
                    updateUiState(uiState.value.copy(isLoading = false))
                }
            }
    }

    fun changeThemeMode(themeMode: ThemeMode) = viewModelScope.launch {
        updateUiState(uiState.value.copy(loadingTheme = true))
        val result = changeThemeModeUseCase.invoke(themeMode)
        if (result.isSuccess) {
            ThemeState.darkTheme.value = themeMode.value == ThemeMode.Dark.value
            updateUiState(uiState.value.copy(loadingTheme = false))
        } else {
            val errorStr = result.exceptionOrNull().toErrorString()
            updateUiState(uiState.value.copy(loadingTheme = false, error = errorStr))
            events.emit(ProfileEvents.OnError(errorStr))
        }
    }

    fun signOut() = viewModelScope.launch {
        updateUiState(uiState.value.copy(isSigningOut = true))
        val result = signOutUseCase.invoke(Unit)
        updateUiState(uiState.value.copy(isSigningOut = false))
        if (result.isSuccess) {
            events.emit(ProfileEvents.SignedOut)
        } else {
            val errorStr = result.exceptionOrNull().toErrorString()
            events.emit(ProfileEvents.OnError(errorStr))
        }
    }
}
