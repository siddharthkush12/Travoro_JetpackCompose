package com.travoro.app.data.remote.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val token: String,
    val user: User,
)

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
)
