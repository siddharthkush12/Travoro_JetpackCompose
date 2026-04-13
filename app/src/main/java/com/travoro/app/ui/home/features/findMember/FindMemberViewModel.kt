package com.travoro.app.ui.home.features.findMember

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travoro.app.core.network.ApiResult
import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.data.remote.dto.liveLocation.MemberLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindMemberViewModel @Inject constructor(
    private val api: TravelApiService,
) : ViewModel() {
    private val _uiState = MutableStateFlow<FindMemberUiState>(FindMemberUiState.Loading)
    val uiState: StateFlow<FindMemberUiState> = _uiState.asStateFlow()

    private var trackingJob: Job? = null

    fun start() {
        viewModelScope.launch {
            checkActiveTrip()
        }
    }

    private suspend fun checkActiveTrip() {
        val result = safeApiCall { api.getActiveTripLite() }

        when (result) {
            is ApiResult.Success -> {
                val trip = result.data.data
                if (trip?._id == null || trip.status != "active") {
                    _uiState.value = FindMemberUiState.NoActiveTrip
                    stopTracking()
                } else {
                    startLiveTracking(trip._id)
                }
            }

            else -> {
                _uiState.value = FindMemberUiState.Error("Unable to fetch trip")
            }
        }
    }

    private fun startLiveTracking(tripId: String) {
        if (trackingJob != null) return

        trackingJob = viewModelScope.launch {
            while (true) {
                val res = safeApiCall { api.getTripLiveLocation(tripId) }

                _uiState.value = when (res) {
                    is ApiResult.Success -> FindMemberUiState.Success(res.data.data)
                    is ApiResult.Error -> FindMemberUiState.Error(res.message)
                    else -> FindMemberUiState.Error("Network error")
                }

                delay(5000)
                checkTripStillActive()
            }
        }
    }

    fun stopTracking() {
        trackingJob?.cancel()
        trackingJob = null
    }

    private suspend fun checkTripStillActive() {
        val result = safeApiCall { api.getActiveTripLite() }
        val trip = (result as? ApiResult.Success)?.data?.data

        if (trip?.status != "active") {
            _uiState.value = FindMemberUiState.NoActiveTrip
            stopTracking()
        }
    }
}

// UI state
sealed class FindMemberUiState {
    object Loading : FindMemberUiState()
    object NoActiveTrip : FindMemberUiState()
    data class Success(val members: List<MemberLocation>) : FindMemberUiState()
    data class Error(val message: String) : FindMemberUiState()
}
