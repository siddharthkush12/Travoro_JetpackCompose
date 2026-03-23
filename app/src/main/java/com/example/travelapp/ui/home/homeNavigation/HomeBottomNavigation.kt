package com.example.travelapp.ui.home.homeNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class HomeBottomNavigation(
    val destination: Any,
    val title: String,
    val icon: ImageVector
) {
    object Home : HomeBottomNavigation(
        destination = HomeTab,
        title = "Home",
        icon = Icons.Filled.Home
    )

    object TravelAI : HomeBottomNavigation(
        destination = TravelAITab,
        title = "TravelAI",
        icon = Icons.Filled.AutoAwesome
    )

    object Messages : HomeBottomNavigation(
        destination = ChatGroupTab,
        title = "Message",
        icon = Icons.Filled.Chat
    )

    object MyTrips : HomeBottomNavigation(
        destination = MyTripsTab,
        title = "My Trips",
        icon = Icons.Filled.CardTravel
    )
}