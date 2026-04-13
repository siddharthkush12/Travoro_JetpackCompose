package com.travoro.app.ui.home.features.addMembers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travoro.app.core.network.ApiResult
import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.data.remote.dto.travelAi.AddMemberRequest
import com.travoro.app.data.remote.dto.travelAi.ChangeTripDateRequest
import com.travoro.app.data.remote.dto.trips.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMembersViewModel @Inject constructor(
    private val travelApiService: TravelApiService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddMembersEvent>(AddMembersEvent.Idle)
    val uiState = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<AddMembersNavigation>()
    val navigation = _navigation.asSharedFlow()

    private val _memberPhone = MutableStateFlow("")
    val memberPhone = _memberPhone.asStateFlow()

    private val _members = MutableStateFlow<List<Profile>>(emptyList())
    val members = _members.asStateFlow()

    private var pendingProfile: Profile? = null
    fun onPhoneChange(value: String) {
        _memberPhone.value = value
    }


    fun searchMember(tripId: String) {
        if (_memberPhone.value.isBlank()) {
            _uiState.value = AddMembersEvent.Error("Enter phone number")
            return
        }

        viewModelScope.launch {
            _uiState.value = AddMembersEvent.Loading

            val response = safeApiCall {
                travelApiService.searchProfileByPhone(
                    phone = _memberPhone.value,
                    tripId = tripId,
                )
            }

            when (response) {
                is ApiResult.Success -> {
                    val profile = response.data.data


                    if (profile.hasConflict && profile.conflictTrip != null) {
                        pendingProfile = profile

                        _uiState.value = AddMembersEvent.SearchConflict(profile)
                    } else {
                        if (_members.value.none {
                                it.userId == profile.userId
                            }) {
                            _members.value += profile
                        }

                        _memberPhone.value = ""

                        _uiState.value = AddMembersEvent.Idle
                    }
                }

                is ApiResult.Error -> {
                    _uiState.value = AddMembersEvent.Error(response.message)
                }

                is ApiResult.Exception -> {
                    _uiState.value = AddMembersEvent.Error(response.message)
                }
            }
        }
    }


    fun changeTripDate(
        tripId: String,
        startDate: String,
        endDate: String,
    ) {
        viewModelScope.launch {
            _uiState.value = AddMembersEvent.Loading

            val response = safeApiCall {
                travelApiService.changeTripDate(
                    tripId,
                    ChangeTripDateRequest(
                        startDate = startDate,
                        endDate = endDate,
                    ),
                )
            }

            when (response) {
                is ApiResult.Success -> {
                    pendingProfile?.let {
                        confirmAddMember(it)
                    }
                    pendingProfile = null
                    _uiState.value = AddMembersEvent.Idle
                }

                is ApiResult.Error -> {
                    _uiState.value = AddMembersEvent.Error(response.message)
                }

                is ApiResult.Exception -> {
                    _uiState.value = AddMembersEvent.Error(response.message)
                }
            }
        }
    }

    private fun confirmAddMember(profile: Profile) {
        if (_members.value.none {
                it.userId == profile.userId
            }) {
            _members.value += profile.copy(hasConflict = false, conflictTrip = null)
        }
        _memberPhone.value = ""
    }


    fun addMembersToTrip(tripId: String) {
        if (_members.value.isEmpty()) {
            viewModelScope.launch {
                _navigation.emit(
                    AddMembersNavigation.NavigateToMyTrip
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.value = AddMembersEvent.Loading
            val response = safeApiCall {
                travelApiService.addMembers(
                    tripId,
                    AddMemberRequest(
                        memberIds = _members.value.map { it.userId },
                    ),
                )
            }

            when (response) {
                is ApiResult.Success -> {
                    _uiState.value = AddMembersEvent.Success

                    _navigation.emit(
                        AddMembersNavigation.NavigateToMyTrip,
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = AddMembersEvent.Error(response.message)
                }

                is ApiResult.Exception -> {
                    _uiState.value = AddMembersEvent.Error(response.message)
                }
            }
        }
    }


    fun removeMember(profile: Profile) {
        _members.value = _members.value.filterNot {
            it.userId == profile.userId
        }
    }

    fun resetState() {
        _uiState.value = AddMembersEvent.Idle
    }


    sealed class AddMembersEvent {
        object Idle : AddMembersEvent()
        object Loading : AddMembersEvent()
        object Success : AddMembersEvent()
        data class SearchConflict(val profile: Profile) : AddMembersEvent()
        data class Error(val message: String) : AddMembersEvent()
    }

    sealed class AddMembersNavigation {
        object NavigateToMyTrip : AddMembersNavigation()
    }

}
