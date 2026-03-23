package com.example.travelapp.data.remote.dto.auth

import kotlinx.serialization.Serializable


@Serializable
data class LoginResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
)


