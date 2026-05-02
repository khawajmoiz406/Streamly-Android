package com.livestreaming.streamly.ui.watch.presentation

import androidx.lifecycle.viewModelScope
import com.livestreaming.streamly.base.BaseViewModel
import com.livestreaming.streamly.config.components.state.FieldState
import com.livestreaming.streamly.config.utils.GenericValidators
import com.livestreaming.streamly.core.model.Comment
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.core.model.User
import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.ui.broadcast.domain.usecase.ObserveStreamCommentsUseCase
import com.livestreaming.streamly.ui.broadcast.domain.usecase.ObserveStreamUseCase
import com.livestreaming.streamly.ui.watch.data.remote.dto.AddCommentRequest
import com.livestreaming.streamly.ui.watch.data.remote.dto.StreamUserRequest
import com.livestreaming.streamly.ui.watch.domain.usecase.AddCommentToStreamUseCase
import com.livestreaming.streamly.ui.watch.domain.usecase.GetStreamUseCase
import com.livestreaming.streamly.ui.watch.domain.usecase.LeaveStreamUseCase
import com.livestreaming.streamly.ui.watch.domain.usecase.UserJoinedStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class WatchViewModel @Inject constructor(
    private val getStreamUseCase: GetStreamUseCase,
    private val leaveStreamUseCase: LeaveStreamUseCase,
    private val observeStreamUseCase: ObserveStreamUseCase,
    private val userJoinedStreamUseCase: UserJoinedStreamUseCase,
    private val addCommentToStreamUseCase: AddCommentToStreamUseCase,
    private val observeStreamCommentsUseCase: ObserveStreamCommentsUseCase,
) : BaseViewModel<WatchUiState, WatchEvents>(WatchUiState()) {
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

    fun getStream(streamId: String) = viewModelScope.launch {
        updateUiState(newUiState = uiState.value.copy(isLoading = true))
        val result = getStreamUseCase.invoke(streamId)
        if (result.isSuccess) {
            val data = result.getOrNull()
            _stream.update { data }
            updateUiState(newUiState = uiState.value.copy(isLoading = false))
            events.emit(WatchEvents.OnStreamFoundFromFB(data))
        } else {
            val error = result.exceptionOrNull()
            val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
            updateUiState(newUiState = uiState.value.copy(error = errorStr, isLoading = false))
            events.emit(WatchEvents.OnError(errorStr))
        }
    }

    fun userJoinedStream(user: User) = viewModelScope.launch {
        stream.value?.let {
            val request = StreamUserRequest(it, user)
            val result = userJoinedStreamUseCase.invoke(request)
            if (!result.isSuccess) {
                val error = result.exceptionOrNull()
                val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
                updateUiState(newUiState = uiState.value.copy(error = errorStr))
            }
        } ?: events.emit(WatchEvents.OnError("Stream cannot be null"))
    }

    fun leaveStream(user: User) = viewModelScope.launch {
        stream.value?.let {
            val request = StreamUserRequest(it, user)
            val result = leaveStreamUseCase.invoke(request)
            if (result.isSuccess) {
                events.emit(WatchEvents.OnLeaveSuccess())
            } else {
                val error = result.exceptionOrNull()
                val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
                updateUiState(newUiState = uiState.value.copy(error = errorStr))
            }
        } ?: events.emit(WatchEvents.OnError("Stream cannot be null"))
    }

    fun addCommentToStream(currentUser: User?) = viewModelScope.launch {
        if (!validateCommentStreamForm()) return@launch
        val request = AddCommentRequest(
            comment = uiState.value.comment.value,
            streamId = stream.value!!.id,
            user = currentUser!!
        )

        updateUiState(newUiState = uiState.value.copy(isAddingComment = true))
        val result = addCommentToStreamUseCase.invoke(request)
        if (result.isSuccess) {
            updateUiState(newUiState = uiState.value.copy(isAddingComment = false))
        } else {
            val error = result.exceptionOrNull()
            val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
            updateUiState(newUiState = uiState.value.copy(error = errorStr, isAddingComment = false))
            events.emit(WatchEvents.OnError(errorStr))
        }
    }

    fun onFieldChange(value: String, fieldUpdater: WatchUiState.(FieldState) -> WatchUiState) {
        updateUiState(uiState.value.fieldUpdater(FieldState(value = value)))
    }

    fun validateCommentStreamForm(): Boolean {
        val validated = uiState.value.copy(
            comment = uiState.value.comment.copy(
                error = GenericValidators.validateField(uiState.value.comment.value),
                isTouched = true
            ),
        )
        updateUiState(validated)

        return validated.comment.error == null
    }
}