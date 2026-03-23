package com.example.travelapp.data.remote.dto.suggestion


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuggestionResponse(
    @SerialName("data")
    val `data`: List<Data?>?,
    @SerialName("success")
    val success: Boolean?
)



@Serializable
data class Data(
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("_id")
    val id: String?,
    @SerialName("image")
    val image: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("updatedAt")
    val updatedAt: String?,
    @SerialName("__v")
    val v: Int?
)