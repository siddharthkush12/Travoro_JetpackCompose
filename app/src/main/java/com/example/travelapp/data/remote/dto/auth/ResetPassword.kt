package com.example.travelapp.data.remote.dto.auth

import kotlinx.serialization.Serializable


@Serializable
data class ResetPasswordRequest(
    val email: String
)



@Serializable
data class ResetPasswordResponse(
    val success: Boolean,
    val message:String
)



