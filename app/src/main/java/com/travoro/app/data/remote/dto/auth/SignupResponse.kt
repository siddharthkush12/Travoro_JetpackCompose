package com.travoro.app.data.remote.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val `data`: Data,
)
