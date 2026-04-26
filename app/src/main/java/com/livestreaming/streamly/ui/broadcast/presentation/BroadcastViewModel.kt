package com.livestreaming.streamly.ui.broadcast.presentation

import androidx.lifecycle.viewModelScope
import com.livestreaming.streamly.base.BaseViewModel
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.ui.broadcast.domain.usecase.EndStreamUseCase
import com.livestreaming.streamly.ui.broadcast.domain.usecase.GoLiveUseCase
import com.livestreaming.streamly.ui.broadcast.domain.usecase.ObserveStreamUseCase
import com.livestreaming.streamly.ui.broadcast.domain.usecase.ToggleCameraUseCase
import com.livestreaming.streamly.ui.broadcast.domain.usecase.ToggleMicUseCase
import com.livestreaming.streamly.ui.broadcast.domain.usecase.TogglePlayPauseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BroadcastViewModel @Inject constructor(
    private val goLiveUseCase: GoLiveUseCase,
    private val endStreamUseCase: EndStreamUseCase,
    private val toggleMicUseCase: ToggleMicUseCase,
    private val toggleCameraUseCase: ToggleCameraUseCase,
    private val observeStreamUseCase: ObserveStreamUseCase,
    private val togglePlayPauseUseCase: TogglePlayPauseUseCase
) : BaseViewModel<BroadcastUiState, BroadcastEvents>(BroadcastUiState()) {
    private val _stream = MutableStateFlow<Stream?>(null)
    val stream: StateFlow<Stream?> = _stream.asStateFlow()

    fun initialize(streamId: String) = viewModelScope.launch {
        observeStreamUseCase.invoke(streamId).collect { stream ->
            stream ?: return@collect
            _stream.update { stream }
        }
    }

    fun goLive() = viewModelScope.launch {
        stream.value?.let {
            val result = goLiveUseCase.invoke(it)
            if (!result.isSuccess) {
                val error = result.exceptionOrNull()
                val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
                updateUiState(newUiState = uiState.value.copy(error = errorStr))
                events.emit(BroadcastEvents.OnError(errorStr))
            }
        } ?: events.emit(BroadcastEvents.OnError("Stream cannot be null"))
    }

    fun toggleMicrophone() = viewModelScope.launch {
        stream.value?.let {
            val result = toggleMicUseCase.invoke(it)
            if (!result.isSuccess) {
                val error = result.exceptionOrNull()
                val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
                updateUiState(newUiState = uiState.value.copy(error = errorStr))
                events.emit(BroadcastEvents.OnError(errorStr))
            }
        } ?: events.emit(BroadcastEvents.OnError("Stream cannot be null"))
    }

    fun toggleCameraOnOff() = viewModelScope.launch {
        stream.value?.let {
            val result = toggleCameraUseCase.invoke(it)
            if (!result.isSuccess) {
                val error = result.exceptionOrNull()
                val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
                updateUiState(newUiState = uiState.value.copy(error = errorStr))
                events.emit(BroadcastEvents.OnError(errorStr))
            }
        } ?: events.emit(BroadcastEvents.OnError("Stream cannot be null"))
    }

    fun togglePlayPause() = viewModelScope.launch {
        stream.value?.let {
            val result = togglePlayPauseUseCase.invoke(it)
            if (!result.isSuccess) {
                val error = result.exceptionOrNull()
                val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
                updateUiState(newUiState = uiState.value.copy(error = errorStr))
                events.emit(BroadcastEvents.OnError(errorStr))
            }
        } ?: events.emit(BroadcastEvents.OnError("Stream cannot be null"))
    }

    fun endStream() = viewModelScope.launch {
        stream.value?.let {
            updateUiState(newUiState = uiState.value.copy(isEndingLiveStream = true))
            val result = endStreamUseCase.invoke(it)
            if (result.isSuccess) {
                updateUiState(newUiState = uiState.value.copy(isEndingLiveStream = false))
                events.emit(BroadcastEvents.OnLiveStreamEndedInFB())
            } else {
                val error = result.exceptionOrNull()
                val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
                updateUiState(newUiState = uiState.value.copy(error = errorStr))
                events.emit(BroadcastEvents.OnError(errorStr))
            }
        } ?: events.emit(BroadcastEvents.OnError("Stream cannot be null"))
    }
}