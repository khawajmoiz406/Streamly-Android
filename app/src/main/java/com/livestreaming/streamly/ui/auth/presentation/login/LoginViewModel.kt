package com.livestreaming.streamly.ui.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.livestreaming.streamly.base.BaseViewModel
import com.livestreaming.streamly.config.components.state.FieldState
import com.livestreaming.streamly.config.utils.GenericValidators
import com.livestreaming.streamly.core.model.AccountType
import com.livestreaming.streamly.core.remote.ApiException
import com.livestreaming.streamly.ui.auth.data.remote.dto.LoginRequest
import com.livestreaming.streamly.ui.auth.domain.usecase.LoginUseCase
import com.livestreaming.streamly.ui.auth.domain.usecase.SendResetPasswordLinkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val resetPasswordLinkUseCase: SendResetPasswordLinkUseCase,
) :
    BaseViewModel<LoginUiState, LoginEvents>(LoginUiState()) {

    fun login(accountType: AccountType) = viewModelScope.launch {
        if (accountType == AccountType.Email && !validateLoginForm()) return@launch

        updateUiState(
            newUiState = uiState.value.copy(
                isSigningWithEmail = accountType == AccountType.Email,
                isSigningWithGoogle = accountType == AccountType.Google
            )
        )
        val request = LoginRequest(
            email = uiState.value.email.value,
            password = uiState.value.password.value,
            accountType = accountType,
        )

        val result = loginUseCase.invoke(request)
        if (result.isSuccess) {
            val data = result.getOrNull()
            updateUiState(
                uiState.value.copy(
                    isSigningWithEmail = false,
                    isSigningWithGoogle = false,
                    forgotEmail = FieldState()
                )
            )
            events.emit(LoginEvents.OnLoginSuccess(data!!))
        } else {
            val error = result.exceptionOrNull()
            val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
            updateUiState(
                uiState.value.copy(
                    isSigningWithEmail = false,
                    isSigningWithGoogle = false,
                    error = errorStr
                )
            )
            events.emit(LoginEvents.OnError(errorStr))
        }

    }

    fun sendResetPasswordLink() = viewModelScope.launch {
        if (!validateResetPasswordForm()) return@launch

        updateUiState(newUiState = uiState.value.copy(isSendingResetLink = true))
        val result = resetPasswordLinkUseCase.invoke(uiState.value.forgotEmail.value)
        if (result.isSuccess) {
            updateUiState(newUiState = uiState.value.copy(isSendingResetLink = false))
            events.emit(LoginEvents.OnLRestLinkSent())
        } else {
            val error = result.exceptionOrNull()
            val errorStr = if (error is ApiException) error.error else error?.localizedMessage ?: ""
            updateUiState(newUiState = uiState.value.copy(isSendingResetLink = false))
            events.emit(LoginEvents.OnError(errorStr))
        }

    }

    fun onFieldChange(value: String, fieldUpdater: LoginUiState.(FieldState) -> LoginUiState) {
        updateUiState(uiState.value.fieldUpdater(FieldState(value = value)))
    }

    fun validateLoginForm(): Boolean {
        val validated = uiState.value.copy(
            email = uiState.value.email.copy(
                error = GenericValidators.validateEmail(uiState.value.email.value),
                isTouched = true
            ),
            password = uiState.value.password.copy(
                error = GenericValidators.validatePassword(uiState.value.password.value),
                isTouched = true
            ),
        )
        updateUiState(validated)

        return listOf(validated.email, validated.password).all { it.error == null }
    }

    fun validateResetPasswordForm(): Boolean {
        val validated = uiState.value.copy(
            forgotEmail = uiState.value.forgotEmail.copy(
                error = GenericValidators.validateEmail(uiState.value.forgotEmail.value),
                isTouched = true
            ),
        )
        updateUiState(validated)

        return validated.forgotEmail.error == null
    }

}