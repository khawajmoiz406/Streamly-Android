package com.livestreaming.streamly.ui.broadcast.presentation

import androidx.lifecycle.viewModelScope
import com.livestreaming.streamly.base.BaseViewModel
import com.livestreaming.streamly.config.components.state.FieldState
import com.livestreaming.streamly.config.utils.GenericValidators
import com.livestreaming.streamly.core.model.Comment
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.model.StreamStatus
import com.livestreaming.streamly.core.model.User
import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.ui.broadcast.data.remote.dto.ChangeStreamStatusRequest
import com.livestreaming.streamly.ui.broadcast.data.remote.dto.StartStreamRequest
import com.livestreaming.streamly.ui.broadcast.domain.usecase.ChangeStreamStatusUseCase
import com.livestreaming.streamly.ui.broadcast.domain.usecase.EndStreamUseCase
import com.livestreaming.streamly.ui.broadcast.domain.usecase.ObserveStreamCommentsUseCase
import com.livestreaming.streamly.ui.broadcast.domain.usecase.ObserveStreamUseCase
import com.livestreaming.streamly.ui.broadcast.domain.usecase.StartStreamUseCase
import com.livestreaming.streamly.ui.broadcast.domain.usecase.ToggleCameraUseCase
import com.livestreaming.streamly.ui.broadcast.domain.usecase.ToggleMicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BroadcastViewModel @Inject constructor(
    private val endStreamUseCase: EndStreamUseCase,
    private val toggleMicUseCase: ToggleMicUseCase,
    private val startStreamUseCase: StartStreamUseCase,
    private val toggleCameraUseCase: ToggleCameraUseCase,
    private val observeStreamUseCase: ObserveStreamUseCase,
    private val changeStreamStatusUseCase: ChangeStreamStatusUseCase,
    private val observeStreamCommentsUseCase: ObserveStreamCommentsUseCase,
) : BaseViewModel<BroadcastUiState, BroadcastEvents>(BroadcastUiState()) {
    private val _stream = MutableStateFlow<Stream?>(null)
    val stream: StateFlow<Stream?> = _stream.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val comments: StateFlow<List<Comment>?> = _stream
        .filter { !it?.id.isNullOrEmpty() }
        .distinctUntilChanged { old, new -> old?.id == new?.id }
        .flatMapLatest { observeStreamCommentsUseCase.invoke(it!!.id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun startObservingStream(streamId: String) = viewModelScope.launch {
        observeStreamUseCase.invoke(streamId).collect { stream ->
            stream ?: return@collect
            _stream.update { stream }
        }
    }

    fun startStream(currentUser: User?) = viewModelScope.launch {
        if (!validateSetupStreamForm()) return@launch
        val request = StartStreamRequest(
            title = uiState.value.title.value,
            desc = uiState.value.description.value,
            user = currentUser!!
        )

        updateUiState(newUiState = uiState.value.copy(isCreatingStream = true))
        val result = startStreamUseCase.invoke(request)
        if (result.isSuccess) {
            val data = result.getOrNull()
            _stream.update { data }
            updateUiState(newUiState = uiState.value.copy(isCreatingStream = false))
            events.emit(BroadcastEvents.OnStreamCreated(data))
        } else {
            val error = result.exceptionOrNull()
            val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
            updateUiState(newUiState = uiState.value.copy(error = errorStr, isCreatingStream = false))
            events.emit(BroadcastEvents.OnError(errorStr))
        }
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

    fun goLive() = viewModelScope.launch {
        stream.value?.let {
            val request = ChangeStreamStatusRequest(it.id, StreamStatus.Live)
            val result = changeStreamStatusUseCase.invoke(request)
            if (!result.isSuccess) {
                val error = result.exceptionOrNull()
                val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
                updateUiState(newUiState = uiState.value.copy(error = errorStr))
                events.emit(BroadcastEvents.OnError(errorStr))
            }
        } ?: events.emit(BroadcastEvents.OnError("Stream cannot be null"))
    }

    fun endStream() = viewModelScope.launch(NonCancellable) {
        stream.value?.let {
            updateUiState(newUiState = uiState.value.copy(isEndingLiveStream = true))
            val result = endStreamUseCase.invoke(it)
            if (result.isSuccess) {
                updateUiState(newUiState = uiState.value.copy(isEndingLiveStream = false))
            } else {
                val error = result.exceptionOrNull()
                val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
                updateUiState(newUiState = uiState.value.copy(error = errorStr))
                events.emit(BroadcastEvents.OnError(errorStr))
            }
        } ?: events.emit(BroadcastEvents.OnError("Stream cannot be null"))
    }


    fun onFieldChange(value: String, fieldUpdater: BroadcastUiState.(FieldState) -> BroadcastUiState) {
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