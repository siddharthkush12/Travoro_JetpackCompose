package com.travoro.app.data.remote.dto.trips

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTripStatusResponse(
    val success: Boolean,
    val message: String,
    val data: TripDto,
)

@Serializable
data class UpdateTripStatusRequest(
    val status: String,
)
