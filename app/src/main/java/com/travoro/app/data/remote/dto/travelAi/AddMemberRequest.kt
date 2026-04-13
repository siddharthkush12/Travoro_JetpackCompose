package com.travoro.app.data.remote.dto.travelAi

data class AddMemberRequest(
    val memberIds: List<String>,
)

data class AddMemberResponse(
    val success: Boolean,
    val message: String,
)

data class ChangeTripDateRequest(
    val startDate: String,
    val endDate: String,
)
