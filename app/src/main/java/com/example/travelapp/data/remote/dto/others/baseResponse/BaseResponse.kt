package com.example.travelapp.data.remote.dto.others.baseResponse

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse(
    val success: Boolean,
    val message: String,
    val code: Int
)