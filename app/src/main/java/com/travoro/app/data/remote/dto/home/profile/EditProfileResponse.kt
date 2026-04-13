package com.travoro.app.data.remote.dto.home.profile

import kotlinx.serialization.Serializable

@Serializable
data class EditProfileResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean,
)

@Serializable
data class Data(
    val _id: String,
    val fullname: String,
    val email: String,
    val phone: String?,
    val gender: String?,
    val city: String?,
    val state: String?,
    val country: String?,
    val profilePic: String,
    val dob: String?,
    val createdAt: String,
    val updatedAt: String,
    val userId: String,
    val __v: Int,
)
