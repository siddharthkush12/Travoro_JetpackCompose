package com.travoro.app.ui.home.features.createTrips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travoro.app.core.network.ApiResult
import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.data.remote.dto.trips.CreateTripRequest
import com.travoro.app.ui.components.getTripDays
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTripViewModel @Inject constructor(
    val travelApiService: TravelApiService,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CreateTripEvent>(CreateTripEvent.Nothing)
    val uiState = _uiState.asStateFlow()
    private val _navigationEvent = MutableSharedFlow<CreateTripNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()
    private val _destination = MutableStateFlow("")
    val destination = _destination.asStateFlow()
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()
    private val _tripType = MutableStateFlow("friends")
    val tripType = _tripType.asStateFlow()
    private val _travelStyle = MutableStateFlow("adventure")
    val travelStyle = _travelStyle.asStateFlow()
    private val _tripBudget = MutableStateFlow("")
    val tripBudget = _tripBudget.asStateFlow()

    private val _tripStartDate = MutableStateFlow("")
    val tripStartDate = _tripStartDate.asStateFlow()

    private val _tripEndDate = MutableStateFlow("")
    val tripEndDate = _tripEndDate.asStateFlow()

    fun onTitleChange(value: String) {
        _title.value = value
    }

    fun onDestinationChange(value: String) {
        _destination.value = value
    }

    fun onDescriptionChange(value: String) {
        _description.value = value
    }

    fun onBudgetChange(value: Int) {
        _tripBudget.value = value.toString()
    }

    fun onTripTypeChange(value: String) {
        _tripType.value = value
    }

    fun onTravelStyleChange(value: String) {
        _travelStyle.value = value
    }

    fun onTripStartDateChange(value: String) {
        _tripStartDate.value = value
    }

    fun onTripEndDateChange(value: String) {
        _tripEndDate.value = value
    }

    fun createTrip() {
        if (_title.value.isBlank()) {
            _uiState.value = CreateTripEvent.Error("Trip title required")

            return
        }

        viewModelScope.launch {
            _uiState.emit(CreateTripEvent.Loading)
            val response = safeApiCall {
                travelApiService.createTrip(
                    CreateTripRequest(
                        title = _title.value,
                        description = _description.value,
                        destination = _destination.value,
                        startDate = _tripStartDate.value,
                        endDate = _tripEndDate.value,
                        tripType = _tripType.value,
                        travelStyle = _travelStyle.value,
                        budget = _tripBudget.value.toIntOrNull(),
                    ),
                )
            }

            when (response) {
                is ApiResult.Success -> {
                    _uiState.value = CreateTripEvent.Success
                    _navigationEvent.emit(
                        CreateTripNavigation.NavigateBack,
                    )
                    val days = getTripDays(_tripStartDate.value, _tripEndDate.value)
                    _navigationEvent.emit(
                        CreateTripNavigation.NavigateToAddMembers(response.data.data.id, days),
                    )
                }

                is ApiResult.Error -> {
                    _uiState.emit(
                        CreateTripEvent.Error(response.message),
                    )
                }

                is ApiResult.Exception -> {
                    _uiState.emit(
                        CreateTripEvent.Error(response.message),
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = CreateTripEvent.Nothing
    }

    sealed class CreateTripNavigation {
        object NavigateBack : CreateTripNavigation()
        data class NavigateToAddMembers(val tripId: String, val days: Int) : CreateTripNavigation()
    }

    sealed class CreateTripEvent {
        object Nothing : CreateTripEvent()
        object Success : CreateTripEvent()
        data class Error(val message: String) : CreateTripEvent()
        object Loading : CreateTripEvent()
    }
}
