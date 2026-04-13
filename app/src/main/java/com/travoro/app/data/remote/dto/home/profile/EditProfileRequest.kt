package com.travoro.app.data.remote.dto.home.profile

import kotlinx.serialization.Serializable

@Serializable
data class EditProfileRequest(
    val fullname: String,
    val profilePic: String,
    val dob: String,
    val phone: String,
    val gender: String,
    val city: String,
    val state: String,
    val country: String,
)
