package com.travoro.app.data.remote.repository

import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.data.remote.dto.trips.BillSplitRequest
import javax.inject.Inject

class BillSplitRepository
    @Inject
    constructor(
        private val apiService: TravelApiService,
    ) {
        suspend fun getBillSplit(
            tripId: String,
            request: BillSplitRequest,
        ) = safeApiCall {
            apiService.getBillSplit(
                tripId = tripId,
                request = request,
            )
        }
    }
