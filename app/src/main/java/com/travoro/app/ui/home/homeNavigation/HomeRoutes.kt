package com.travoro.app.ui.home.homeNavigation

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
data class MyTripDetail(
    val trip: String,
)

@Serializable
object MyProfileTab

@Serializable
object ChatGroupTab

@Serializable
object CreateTripTab

@Serializable
data class AddMembersTab(
    val tripId: String,
    val days: Int,
)

@Serializable
data class MessageScreenTab(
    val groupId: String,
)

@Serializable
object MyAccount

@Serializable
object Support

@Serializable
object Notification

@Serializable
object DeveloperTab

@Serializable
object BillSplit

@Serializable
data class SplitExpense(
    val trip: String,
)

@Serializable
object Sos

@Serializable
object AiSearch

@Serializable
object FindMember
