package com.example.travelapp.data.remote.dto.auth

import kotlinx.serialization.Serializable


@Serializable
data class SignupRequest(
    val name:String,
    val email:String,
    val password:String
)