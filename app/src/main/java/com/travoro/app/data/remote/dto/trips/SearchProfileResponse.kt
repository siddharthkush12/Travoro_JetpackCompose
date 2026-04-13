package com.travoro.app.data.remote.dto.trips

data class SearchProfileResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val data: Profile,
)

data class Profile(
    val userId: String,
    val fullname: String,
    val phone: String,
    val profilePic: String?,
    val hasConflict: Boolean = false,
    val conflictTrip: ConflictTrip? = null,
)

data class ConflictTrip(
    val title: String,
    val startDate: String,
    val endDate: String,
)
