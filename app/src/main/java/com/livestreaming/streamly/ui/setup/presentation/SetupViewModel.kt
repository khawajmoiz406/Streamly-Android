package com.livestreaming.streamly.ui.setup.presentation

import androidx.lifecycle.viewModelScope
import com.livestreaming.streamly.base.BaseViewModel
import com.livestreaming.streamly.config.components.state.FieldState
import com.livestreaming.streamly.config.utils.GenericValidators
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.ui.setup.data.remote.dto.StartStreamRequest
import com.livestreaming.streamly.ui.setup.domain.usecase.StartStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(private val startStreamUseCase: StartStreamUseCase) :
    BaseViewModel<SetupUiState, SetupEvents>(SetupUiState()) {
    var stream: Stream? = null

    fun startStream() = viewModelScope.launch {
        if (!validateSetupStreamForm()) return@launch
        val request = StartStreamRequest(
            title = uiState.value.title.value,
            desc = uiState.value.description.value
        )

        updateUiState(newUiState = uiState.value.copy(isLoading = true))
        val result = startStreamUseCase.invoke(request)
        if (result.isSuccess) {
            stream = result.getOrNull()
            updateUiState(newUiState = uiState.value.copy(isLoading = false))
            events.emit(SetupEvents.OnStreamStarted())
        } else {
            val error = result.exceptionOrNull()
            val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
            updateUiState(newUiState = uiState.value.copy(isLoading = false, error = errorStr))
            events.emit(SetupEvents.OnError(errorStr))
        }

    }

    fun onFieldChange(value: String, fieldUpdater: SetupUiState.(FieldState) -> SetupUiState) {
        updateUiState(uiState.value.fieldUpdater(FieldState(value = value)))
    }

    fun validateSetupStreamForm(): Boolean {
        val validated = uiState.value.copy(
            title = uiState.value.title.copy(
                error = GenericValidators.validateField(uiState.value.title.value),
                isTouched = true
            ),
        )
        updateUiState(validated)

        return validated.title.error == null
    }
}