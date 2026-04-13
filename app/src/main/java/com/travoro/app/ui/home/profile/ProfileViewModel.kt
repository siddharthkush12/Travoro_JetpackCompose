package com.travoro.app.ui.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travoro.app.core.network.ApiResult
import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.data.remote.dto.home.profile.Data
import com.travoro.app.di.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val travelApiService: TravelApiService,
    val session: Session,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileEvent>(ProfileEvent.Idle)
    val uiState = _uiState.asStateFlow()

    private val _profile = MutableStateFlow<Data?>(null)
    val profile = _profile.asStateFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            _uiState.emit(ProfileEvent.Loading)
            val response = safeApiCall {
                travelApiService.fetchProfile()
            }

            when (response) {
                is ApiResult.Success -> {
                    _profile.value = response.data.data
                    _uiState.value = (ProfileEvent.Success)
                }

                is ApiResult.Error -> {
                    _uiState.emit(ProfileEvent.Error(response.message))
                }

                is ApiResult.Exception -> {
                    _uiState.emit(ProfileEvent.Error(response.message))
                }
            }
        }
    }

    fun updateProfile(
        profilePic: MultipartBody.Part?,
        fullname: String,
        dob: String,
        gender: String,
        phone: String,
        city: String,
        state: String,
        country: String,
    ) {
        viewModelScope.launch {
            _uiState.emit(ProfileEvent.Loading)
            val response = safeApiCall {
                travelApiService.editProfile(
                    profilePic = profilePic,
                    fullname = fullname.toRequestBody(),
                    dob = dob.toRequestBody(),
                    gender = gender.toRequestBody(),
                    phone = phone.toRequestBody(),
                    city = city.toRequestBody(),
                    state = state.toRequestBody(),
                    country = country.toRequestBody(),
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    _profile.value = response.data.data
                    _uiState.value = ProfileEvent.UpdateSuccess
                    session.removeProfileImage()
                    session.storeProfileImage(response.data.data.profilePic)
                }

                is ApiResult.Error -> {
                    _uiState.emit(ProfileEvent.Error(response.message))
                }

                is ApiResult.Exception -> {
                    _uiState.emit(ProfileEvent.Error(response.message))
                }
            }
        }
    }


    sealed class ProfileEvent {
        object Idle : ProfileEvent()
        object Loading : ProfileEvent()
        object Success : ProfileEvent()
        object UpdateSuccess : ProfileEvent()
        data class Error(val message: String) : ProfileEvent()
    }
}
