package com.travoro.app.data.remote.repository

import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.data.remote.dto.trips.UpdateTripStatusRequest
import javax.inject.Inject

class TripRepository
    @Inject
    constructor(
        private val apiService: TravelApiService,
    ) {
        suspend fun getMyTrips() =
            safeApiCall {
                apiService.getMyTrips()
            }

        suspend fun updateTripStatus(
            tripId: String,
            status: String,
        ) = safeApiCall {
            apiService.updateTripStatus(tripId, UpdateTripStatusRequest(status))
        }

        suspend fun leaveTrip(tripId: String) =
            safeApiCall {
                apiService.leaveTrip(tripId)
            }
    }
