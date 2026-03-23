package com.example.travelapp.ui.home.navigationDrawer.myaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.data.remote.api.TravelApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val travelApiService: TravelApiService
) : ViewModel() {

    private val _changePasswordState =
        MutableStateFlow<ChangePasswordState>(ChangePasswordState.Idle)
    val changePasswordState = _changePasswordState.asStateFlow()

    private val _deleteAccountState =
        MutableStateFlow<DeleteAccountState>(DeleteAccountState.Idle)
    val deleteAccountState = _deleteAccountState.asStateFlow()

    private val _oldPassword = MutableStateFlow("")
    val oldPassword = _oldPassword.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword = _newPassword.asStateFlow()

    fun onOldPasswordChange(value: String) {
        _oldPassword.value = value
    }

    fun onNewPasswordChange(value: String) {
        _newPassword.value = value
    }

    fun changePassword() {

        val old = _oldPassword.value
        val new = _newPassword.value

        if (old.isBlank() || new.isBlank()) {
            _changePasswordState.value =
                ChangePasswordState.Error("⚠️ Fill all fields")
            return
        }

        if (new.length < 6) {
            _changePasswordState.value =
                ChangePasswordState.Error("Password must be 6+ characters")
            return
        }

        viewModelScope.launch {

            _changePasswordState.value = ChangePasswordState.Loading

//            val response = safeApiCall {
//                api.changePassword(
//                    mapOf(
//                        "oldPassword" to old,
//                        "newPassword" to new
//                    )
//                )
//            }
//
//            _changePasswordState.value = when (response) {
//                is ApiResult.Success -> ChangePasswordState.Success("Password updated")
//                is ApiResult.Error -> ChangePasswordState.Error(response.message)
//                is ApiResult.Exception -> ChangePasswordState.Error(response.message)
//            }
        }
    }

    fun resetPasswordState() {
        _changePasswordState.value = ChangePasswordState.Idle
    }


    fun deleteAccount() {

        viewModelScope.launch {

            _deleteAccountState.value = DeleteAccountState.Loading

//            val response = safeApiCall {
//                api.deleteAccount()
//            }
//
//            _deleteAccountState.value = when (response) {
//                is ApiResult.Success -> DeleteAccountState.Success("Account deleted")
//                is ApiResult.Error -> DeleteAccountState.Error(response.message)
//                is ApiResult.Exception -> DeleteAccountState.Error(response.message)
//            }
        }
    }

    fun resetDeleteState() {
        _deleteAccountState.value = DeleteAccountState.Idle
    }


    sealed class ChangePasswordState {
        object Idle : ChangePasswordState()
        object Loading : ChangePasswordState()
        data class Success(val message: String) : ChangePasswordState()
        data class Error(val message: String) : ChangePasswordState()
    }


    sealed class DeleteAccountState {
        object Idle : DeleteAccountState()
        object Loading : DeleteAccountState()
        data class Success(val message: String) : DeleteAccountState()
        data class Error(val message: String) : DeleteAccountState()
    }


}