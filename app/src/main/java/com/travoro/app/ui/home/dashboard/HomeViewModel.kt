package com.travoro.app.ui.home.dashboard

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.Priority
import com.travoro.app.core.network.ApiResult
import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.data.remote.dto.suggestion.Data
import com.travoro.app.ui.utils.LocationHelper
import com.travoro.app.ui.utils.TripLocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val travelApiService: TravelApiService,
) : ViewModel() {
    private var trackingJob: Job? = null
    private var runningTripId: String? = null
    private val _uiState = MutableStateFlow<HomeEvent>(HomeEvent.Idle)
    val uiState = _uiState.asStateFlow()

    private val locationHelper = LocationHelper(context)

    private val _location = MutableStateFlow<Pair<Double, Double>?>(null)
    val location = _location.asStateFlow()
    private val _city = MutableStateFlow<String>("")
    val city = _city.asStateFlow()

    private val _trendingDestination =
        MutableStateFlow<TrendingDestinationEvent>(TrendingDestinationEvent.Idle)
    val trendingDestination = _trendingDestination.asStateFlow()

    init {
        fetchLocationAndCity()
        fetchTrendingDestination()
    }

    fun fetchLocationAndCity() {
        viewModelScope.launch {
            _uiState.value = HomeEvent.Loading

            val loc = locationHelper.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY)
            if (loc == null) {
                _uiState.value = HomeEvent.Error("Unable to fetch location")
                return@launch
            }
            val latitude = loc.latitude
            val longitude = loc.longitude

            _location.value = Pair(latitude, longitude)

            val cityName = locationHelper.getCityFromLocation(context, latitude, longitude)
            _city.value = cityName ?: ""
        }
    }

    fun fetchTrendingDestination() {
        viewModelScope.launch {
            _uiState.value = HomeEvent.Loading

            val response = safeApiCall {
                travelApiService.getTrendingDestination()
            }
            when (response) {
                is ApiResult.Success -> {
                    val apiList = response.data.data ?: emptyList()
                    _trendingDestination.value = TrendingDestinationEvent.Success(apiList)
                }

                is ApiResult.Exception -> {
                    _trendingDestination.value = TrendingDestinationEvent.Error(response.message)
                }

                is ApiResult.Error -> {
                    _trendingDestination.value = TrendingDestinationEvent.Error(response.message)
                }
            }
        }
    }

    fun startLocationForActiveTrips(userId: String?) {
        trackingJob?.cancel()

        trackingJob = viewModelScope.launch {
            while (isActive) {
                val response = safeApiCall {
                    travelApiService.getActiveTripLite()
                }

                if (response is ApiResult.Success) {
                    val trip = response.data.data

                    if (trip?.status == "active" && isLocationEnabled(context)) {
                        if (runningTripId != trip._id) {
                            runningTripId = trip._id
                            val intent = Intent(
                                context,
                                TripLocationService::class.java,
                            )
                            intent.putExtra("tripId", trip._id)
                            intent.putExtra("userId", userId)
                            ContextCompat.startForegroundService(
                                context,
                                intent,
                            )
                        }
                    } else {
                        stopLocationService()
                    }
                } else {
                    stopLocationService()
                }

                delay(60000)
            }
        }
    }

    private fun stopLocationService() {
        runningTripId = null
        val intent = Intent(
            context,
            TripLocationService::class.java,
        )
        context.stopService(intent)
    }

    private fun isLocationEnabled(context: Context): Boolean {
        return context.getSharedPreferences("trip_pref", Context.MODE_PRIVATE)
            .getBoolean("live_location_enabled", false)
    }


    sealed class TrendingDestinationEvent {
        object Idle : TrendingDestinationEvent()
        object Loading : TrendingDestinationEvent()
        data class Success(val data: List<Data?>?) : TrendingDestinationEvent()
        data class Error(val message: String) : TrendingDestinationEvent()
    }

    sealed class HomeEvent {
        object Idle : HomeEvent()
        object Loading : HomeEvent()
        object Success : HomeEvent()
        data class Error(val message: String) : HomeEvent()
    }
}
