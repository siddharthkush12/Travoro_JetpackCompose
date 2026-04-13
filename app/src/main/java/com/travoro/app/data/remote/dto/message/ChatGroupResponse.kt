package com.travoro.app.data.remote.dto.message

data class ChatGroupResponse(
    val success: Boolean,
    val data: List<ChatGroup>,
)

data class ChatGroup(
    val _id: String,
    val name: String,
    val trip: Trip,
    val updatedAt: String,
)

data class Trip(
    val _id: String,
    val title: String,
    val destination: String,
    val coverImage: String?,
)
