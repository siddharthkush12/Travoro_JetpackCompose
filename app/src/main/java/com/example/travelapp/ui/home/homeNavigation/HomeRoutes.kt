package com.example.travelapp.ui.home.homeNavigation

import kotlinx.serialization.Serializable


@Serializable
object HomeTab

@Serializable
object TravelAITab

@Serializable
object SearchTab


@Serializable
object MyTripsTab


@Serializable
object MyProfileTab




@Serializable
object ChatGroupTab


@Serializable
object CreateTripTab


@Serializable
data class AddMembersTab(
    val tripId: String
)



@Serializable
data class MessageScreenTab(
    val groupId: String
)



@Serializable
object MyAccount


@Serializable
object Support


@Serializable
object Notification


@Serializable
object DeveloperTab