package com.travoro.app.data.remote.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val email: String,
)

@Serializable
data class ResetPasswordResponse(
    val success: Boolean,
    val message: String,
)

@Serializable
data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
)

@Serializable
data class ChangePasswordResponse(
    val success: Boolean,
    val message: String,
)
