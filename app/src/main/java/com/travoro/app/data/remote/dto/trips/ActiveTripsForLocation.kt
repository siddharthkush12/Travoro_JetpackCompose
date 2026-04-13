package com.travoro.app.data.remote.dto.trips

import kotlinx.serialization.Serializable

@Serializable
data class ActiveTripLiteResponseDto(
    val success: Boolean,
    val data: ActiveTripLiteDto? = null,
)

@Serializable
data class ActiveTripLiteDto(
    val _id: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val status: String? = null,
    val coverImage: String? = null,
    val destination: String? = null,
)
