package com.livestreaming.streamly.ui.home.presentation

import androidx.lifecycle.viewModelScope
import com.livestreaming.streamly.base.BaseViewModel
import com.livestreaming.streamly.core.model.Stream
import com.livestreaming.streamly.ui.home.domain.usecase.GetLiveStreamsUseCase
import com.livestreaming.streamly.ui.home.domain.usecase.ObserveLiveStreamsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLiveStreamsUseCase: GetLiveStreamsUseCase,
    private val observeLiveStreamsUseCase: ObserveLiveStreamsUseCase,
) : BaseViewModel<HomeUiState, Unit>(HomeUiState()) {

    private val _liveStreams = MutableStateFlow<List<Stream>?>(null)
    val liveStreams: StateFlow<List<Stream>?> = _liveStreams.asStateFlow()

    init {
        getLiveStreams()
    }

    private fun observeLiveStreamChange() = viewModelScope.launch {
        observeLiveStreamsUseCase.invoke(Unit).collect { updatedStreams ->
            val updatedMap = updatedStreams?.associateBy { it.id } ?: emptyMap()
            val currentMap = _liveStreams.value?.associateBy { it.id } ?: emptyMap()

            _liveStreams.value = updatedMap.values.map { updated ->
                val existing = currentMap[updated.id]

                // End stream case will automatically be handled because the streams that are not
                // live won't be present in updateStreams
                when {
                    // New stream went live so we will add it
                    existing == null -> updated

                    // Status or viewers count changed
                    existing.status != updated.status || existing.viewerCount != updated.viewerCount ->
                        existing.copy(status = updated.status, viewerCount = updated.viewerCount)

                    // Nothing changed so no need to create new instance so it wont recompose
                    else -> existing
                }
            }
        }
    }

    private fun getLiveStreams() = viewModelScope.launch {
        updateUiState(uiState.value.copy(isLoading = true))
        val result = getLiveStreamsUseCase.invoke(Unit)
        if (result.isSuccess) {
            val data = result.getOrNull()
            _liveStreams.update { data }
            observeLiveStreamChange()
            updateUiState(uiState.value.copy(isLoading = false))
        } else {
            val errorStr = result.exceptionOrNull().toErrorString()
            updateUiState(uiState.value.copy(isLoading = false, error = errorStr))
        }
    }
}