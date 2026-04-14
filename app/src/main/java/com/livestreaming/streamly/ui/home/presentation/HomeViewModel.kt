package com.livestreaming.streamly.ui.home.presentation

import androidx.lifecycle.viewModelScope
import com.livestreaming.streamly.base.BaseViewModel
import com.livestreaming.streamly.ui.home.domain.usecase.GetHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getHomeUseCase: GetHomeUseCase) :
    BaseViewModel<HomeUiState, Unit>(HomeUiState()) {

    init {
        getHomeFilters()
    }

    private fun getHomeFilters() = viewModelScope.launch {
        updateUiState(uiState.value.copy(isLoading = true))
        val result = getHomeUseCase.invoke(Unit)
        if (result.isSuccess) {
            val data = result.getOrNull()
            updateUiState(uiState.value.copy(isLoading = false))
        } else {
            val errorStr = result.exceptionOrNull().toErrorString()
            updateUiState(uiState.value.copy(isLoading = false, error = errorStr))
        }
    }
}