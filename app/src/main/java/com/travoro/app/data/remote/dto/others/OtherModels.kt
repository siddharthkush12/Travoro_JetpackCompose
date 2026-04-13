package com.travoro.app.data.remote.dto.others

import androidx.compose.ui.graphics.vector.ImageVector

data class Destination(
    val name: String,
    val country: String,
    val imageUrl: String,
    val rating: String,
)

data class TravelOption(
    val title: String,
    val icon: ImageVector,
    val route: Any,
    val isAlert: Boolean = false,
)
