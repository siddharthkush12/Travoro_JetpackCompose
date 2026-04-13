package com.travoro.app.data.remote.dto.travelAi

data class AiTripRequest(
    val budget: Int,
    val days: Int,
    val travelStyle: String,
    val groupType: String,
    val currentCity: String,
    val startDate: String,
)
