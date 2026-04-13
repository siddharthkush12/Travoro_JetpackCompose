package com.travoro.app.data.remote.dto.suggestion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuggestionResponse(
    @SerialName("data")
    val `data`: List<Data?>?,
    @SerialName("success")
    val success: Boolean?,
)

@Serializable
data class Data(
    val createdAt: String?,
    val description: String?,
    @SerialName("_id")
    val id: String?,
    val image: String?,
    val title: String?,
    val updatedAt: String?,
    val rating: String?,
)
