package com.livestreaming.streamly.ui.auth.presentation.register

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.livestream.streamly.R
import com.livestreaming.streamly.base.BaseViewModel
import com.livestreaming.streamly.config.components.state.FieldState
import com.livestreaming.streamly.config.utils.GenericValidators
import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.ui.auth.data.remote.dto.RegisterRequest
import com.livestreaming.streamly.ui.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
    BaseViewModel<RegisterUiState, RegisterEvents>(RegisterUiState()) {

    fun register() = viewModelScope.launch {
        if (!validateRegisterForm()) return@launch

        updateUiState(uiState.value.copy(isLoading = true))
        val request = RegisterRequest(
            name = uiState.value.name.value,
            email = uiState.value.email.value,
            phoneNumber = uiState.value.phoneNumber.value,
            password = uiState.value.password.value,
            profilePicture = uiState.value.profilePicture
        )

        val result = registerUseCase.invoke(request)
        if (result.isSuccess) {
            val data = result.getOrNull()
            updateUiState(uiState.value.copy(isLoading = false))
            events.emit(RegisterEvents.OnRegisterSuccess(data!!))
        } else {
            val error = result.exceptionOrNull()
            val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
            updateUiState(uiState.value.copy(isLoading = false))
            events.emit(RegisterEvents.OnError(errorStr))
        }

    }

    fun onFieldChange(value: String, fieldUpdater: RegisterUiState.(FieldState) -> RegisterUiState) {
        updateUiState(uiState.value.fieldUpdater(FieldState(value = value)))
    }

    fun onPictureChanged(value: Uri) {
        updateUiState(uiState.value.copy(profilePicture = value))
    }

    fun validateRegisterForm(): Boolean {
        val validated = uiState.value.copy(
            name = uiState.value.name.copy(
                error = GenericValidators.validateField(uiState.value.name.value),
                isTouched = true
            ),
            email = uiState.value.email.copy(
                error = GenericValidators.validateEmail(uiState.value.email.value),
                isTouched = true
            ),
            phoneNumber = uiState.value.phoneNumber.copy(
                error = GenericValidators.validatePhoneNumber(uiState.value.phoneNumber.value),
                isTouched = true
            ),
            password = uiState.value.password.copy(
                error = GenericValidators.validatePassword(uiState.value.password.value),
                isTouched = true
            ),
            confirmPassword = uiState.value.confirmPassword.copy(
                error = if (uiState.value.confirmPassword != uiState.value.password) R.string.passwords_must_match else null,
                isTouched = true
            ),
        )
        updateUiState(validated)

        return listOf(
            validated.name,
            validated.email,
            validated.phoneNumber,
            validated.password,
            validated.confirmPassword
        ).all { it.error == null }
    }
}