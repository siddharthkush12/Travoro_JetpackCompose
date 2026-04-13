package com.travoro.app.data.remote.dto.home.profile

import kotlinx.serialization.Serializable

@Serializable
data class FetchProfileResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean,
)
