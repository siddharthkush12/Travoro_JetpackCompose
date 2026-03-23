package com.example.travelapp.ui.auth.signup



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.AppInitializer
import com.example.travelapp.core.network.ApiResult
import com.example.travelapp.core.network.safeApiCall
import com.example.travelapp.data.remote.api.TravelApiService
import com.example.travelapp.data.remote.dto.auth.SignupRequest
import com.example.travelapp.di.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignupViewModel @Inject constructor(
    val travelApiService: TravelApiService,
    val session: Session,
    val appInitializer: AppInitializer
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpEvent>(SignUpEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignUpNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()


    private val _fullName = MutableStateFlow("")
    val fullName = _fullName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()


    fun onNameChange(fullName: String) {
        _fullName.value = fullName
    }


    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }


    fun onSignUpButtonClick() {
        viewModelScope.launch {
            _uiState.emit(SignUpEvent.Loading)

            val response = safeApiCall {
                travelApiService.signup(
                    SignupRequest(
                        name = _fullName.value,
                        email = _email.value,
                        password = _password.value
                    )
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    _uiState.value = SignUpEvent.Success
                    session.storeToken(response.data.data.token)
                    session.storeUserId(response.data.data.user.id)
                    appInitializer.initializeApp(viewModelScope)
                    _navigationEvent.emit(SignUpNavigation.NavigationToHome)
                }

                is ApiResult.Error -> {
                    _uiState.emit(
                        SignUpEvent.Error(
                            response.message
                        )
                    )
                }

                is ApiResult.Exception -> {
                    _uiState.emit(
                        SignUpEvent.Error(
                            response.message
                        )
                    )
                }
            }
        }

    }


    fun onClearError() {
        viewModelScope.launch {
            _uiState.value = SignUpEvent.Nothing
        }
    }


    sealed class SignUpNavigation {
        object NavigationToHome : SignUpNavigation()
    }

    sealed class SignUpEvent {
        object Nothing : SignUpEvent()
        object Success : SignUpEvent()
        data class Error(val message: String) : SignUpEvent()
        object Loading : SignUpEvent()
    }


}