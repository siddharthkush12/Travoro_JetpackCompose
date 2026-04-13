package com.travoro.app.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travoro.app.AppInitializer
import com.travoro.app.core.network.ApiResult
import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.data.remote.dto.auth.LoginRequest
import com.travoro.app.data.remote.dto.auth.ResetPasswordRequest
import com.travoro.app.di.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val travelApiService: TravelApiService,
    val session: Session,
    val appInitializer: AppInitializer,
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginEvent>(LoginEvent.Nothing)
    val uiState = _uiState.asStateFlow()
    private val _navigationEvent = MutableSharedFlow<LoginNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()
    private val _forgotPasswordState =
        MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Idle)
    val forgotState = _forgotPasswordState.asStateFlow()
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    private val _forgetEmail = MutableStateFlow("")
    val forgetEmail = _forgetEmail.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onForgetEmailChange(email: String) {
        _forgetEmail.value = email
    }

    fun onLoginButtonClick() {
        viewModelScope.launch {
            _uiState.emit(LoginEvent.Loading)
            val response = safeApiCall {
                travelApiService.login(
                    LoginRequest(
                        email = _email.value,
                        password = _password.value,
                    ),
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    _uiState.value = LoginEvent.Success
                    session.storeToken(response.data.data.token)
                    session.storeUserId(response.data.data.user.id)
                    appInitializer.initializeApp(viewModelScope)
                    _navigationEvent.emit(LoginNavigation.NavigationToHome)
                }
                is ApiResult.Error -> {
                    _uiState.emit(
                        LoginEvent.Error(
                            response.message,
                        ),
                    )
                }
                is ApiResult.Exception -> {
                    _uiState.emit(
                        LoginEvent.Error(
                            response.message,
                        ),
                    )
                }
            }
        }
    }

    fun onForgetPasswordButtonClick() {
        if (_forgetEmail.value.isBlank()) {
            _forgotPasswordState.value = ForgotPasswordState.Error("Please enter registered email")
            return
        }
        viewModelScope.launch {
            _forgotPasswordState.value = ForgotPasswordState.Loading
            val response = safeApiCall {
                travelApiService.resetPassword(
                    ResetPasswordRequest(
                        email = _forgetEmail.value,
                    ),
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    _forgotPasswordState.value =
                        ForgotPasswordState.Success("Password reset link sent to your email")
                }

                is ApiResult.Error -> {
                    _forgotPasswordState.value = ForgotPasswordState.Error(response.message)
                }

                else -> {
                    _forgotPasswordState.value = ForgotPasswordState.Error("Something went wrong")
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = LoginEvent.Nothing
    }

    fun clearForgetPasswordState() {
        _forgotPasswordState.value = ForgotPasswordState.Idle
    }

    sealed class LoginNavigation {
        object NavigationToHome : LoginNavigation()
    }

    sealed class ForgotPasswordState {
        object Idle : ForgotPasswordState()
        object Loading : ForgotPasswordState()
        data class Success(val message: String) : ForgotPasswordState()
        data class Error(val message: String) : ForgotPasswordState()
    }

    sealed class LoginEvent {
        object Nothing : LoginEvent()
        object Success : LoginEvent()
        data class Error(val message: String) : LoginEvent()
        object Loading : LoginEvent()
    }
}
