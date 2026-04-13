package com.travoro.app.ui.home.mytrips

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travoro.app.core.network.ApiResult
import com.travoro.app.data.remote.dto.trips.TripDto
import com.travoro.app.data.remote.repository.TripRepository
import com.travoro.app.ui.utils.TripLocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTripsViewModel @Inject constructor(
    private val repository: TripRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MyTripsState>(MyTripsState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _leaveTripState = MutableStateFlow<LeaveTripEvent>(
        LeaveTripEvent.Idle,
    )


    fun fetchMyTrips() {
        viewModelScope.launch {
            _uiState.value = MyTripsState.Loading
            when (val response = repository.getMyTrips()) {
                is ApiResult.Success -> {
                    _uiState.value = MyTripsState.Success(response.data.data)
                }

                is ApiResult.Error -> {
                    _uiState.value = MyTripsState.Error(response.message)
                }

                is ApiResult.Exception -> {
                    _uiState.value = MyTripsState.Error(response.message)
                }
            }
        }
    }

    fun leaveTrip(tripId: String) {
        viewModelScope.launch {
            _leaveTripState.value = LeaveTripEvent.Loading

            when (val response = repository.leaveTrip(tripId)) {
                is ApiResult.Success -> {
                    _leaveTripState.value = LeaveTripEvent.Success
                }

                is ApiResult.Error -> {
                    _leaveTripState.value = LeaveTripEvent.Error(
                        response.message,
                    )
                }

                is ApiResult.Exception -> {
                    _leaveTripState.value = LeaveTripEvent.Error(
                        response.message,
                    )
                }
            }
        }
    }

    fun startLiveLocation(
        context: Context,
        tripId: String,
        userId: String?,
    ) {
        val intent = Intent(
            context,
            TripLocationService::class.java,
        )
        intent.putExtra("tripId", tripId)
        intent.putExtra("userId", userId)

        ContextCompat.startForegroundService(
            context,
            intent,
        )
    }

    fun stopLiveLocation(context: Context) {
        context.stopService(
            Intent(
                context,
                TripLocationService::class.java,
            ),
        )
    }

    fun saveLocationPreference(
        context: Context,
        enabled: Boolean,
    ) {
        context.getSharedPreferences(
            "trip_pref",
            Context.MODE_PRIVATE,
        ).edit {
            putBoolean("live_location_enabled", enabled)
        }
    }


    sealed class LeaveTripEvent {
        object Idle : LeaveTripEvent()

        object Loading : LeaveTripEvent()

        object Success : LeaveTripEvent()

        data class Error(
            val message: String,
        ) : LeaveTripEvent()
    }

    sealed class MyTripsState {
        object Idle : MyTripsState()

        object Loading : MyTripsState()

        data class Success(val trips: List<TripDto>) : MyTripsState()

        data class Error(val message: String) : MyTripsState()
    }
}
