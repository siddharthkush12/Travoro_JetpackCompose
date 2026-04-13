package com.travoro.app.data.remote.dto.travelAi

import com.google.gson.annotations.SerializedName

data class AcceptTripResponse(
    val success: Boolean,
    val message: String,
    val data: SavedTrip,
)

data class SavedTrip(
    @SerializedName("_id")
    val id: String,
    val title: String,
    val description: String,
    val destination: String,
    val latitude: Double?,
    val longitude: Double?,
    val plannerType: String,
    val tripType: String,
    val travelStyle: String,
    val budget: Int,
    val status: String,
    val createdBy: String,
    val members: List<Member>,
    val totalExpense: Int,
    val coverImage: String?,
    val images: List<String>,
    val itinerary: List<Itinerary>,
    val hotels: List<Hotel>,
    val restaurants: List<Restaurant>,
    val budgetBreakdown: BudgetBreakdown?,
    val travelTips: List<String>,
    val bestTimeToVisit: String?,
    val recommendedTransport: String?,
    val balances: List<Balance>,
    val createdAt: String,
    val updatedAt: String,
)

data class Member(
    val user: String,
    val role: String,
)

data class Balance(
    val user: String,
    val amount: Int,
)
