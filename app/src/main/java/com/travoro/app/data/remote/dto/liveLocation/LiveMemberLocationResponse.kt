package com.travoro.app.data.remote.dto.liveLocation

import kotlinx.serialization.Serializable

@Serializable
data class LiveMemberLocationResponse(
    val success: String,
    val data: List<MemberLocation>,
)

@Serializable
data class MemberLocation(
    val userId: String,
    val name: String,
    val image: String,
    val lat: Double,
    val lng: Double,
    val updatedAt: String,
)
