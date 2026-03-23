package com.example.travelapp.data.remote.dto.auth

import kotlinx.serialization.Serializable


@Serializable
data class SignupResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val `data`: Data,
)

