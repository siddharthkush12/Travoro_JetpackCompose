package com.travoro.app.data.remote.dto.message

data class MessageResponse(
    val success: Boolean,
    val data: List<Message>,
)

data class Message(
    val _id: String,
    val chatGroup: String,
    val sender: Sender,
    val text: String?,
    val type: String,
    val mediaUrl: String?,
    val createdAt: String,
)

data class Sender(
    val _id: String,
    val fullname: String?,
    val profilePic: String?,
)
