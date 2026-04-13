package com.travoro.app.data.remote.dto.travelAi

import java.util.UUID.randomUUID

data class AiChatMessage(
    val id: String = randomUUID().toString(),
    val text: String? = null,
    val isUser: Boolean = false,
    val options: List<String>? = null,
    val tripResult: AiTripData? = null,
)

data class DeleteTripResponse(
    val success: Boolean,
    val message: String,
)
