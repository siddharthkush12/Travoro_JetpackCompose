package com.travoro.app.data.remote.dto.trips

import kotlinx.serialization.Serializable

@Serializable
data class MyTripsResponse(
    val success: Boolean,
    val message: String,
    val data: List<TripDto>,
)

@Serializable
data class TripDto(
    val _id: String,
    val title: String,
    val description: String? = null,
    val destination: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val plannerType: String? = null,
    val tripType: String? = null,
    val travelStyle: String? = null,
    val budget: Int? = null,
    val status: String? = null,
    val createdBy: String,
    val coverImage: String? = null,
    val images: List<String> = emptyList(),
    val itinerary: List<ItineraryDto> = emptyList(),
    val hotels: List<HotelDto> = emptyList(),
    val restaurants: List<RestaurantDto> = emptyList(),
    val travelTips: List<String> = emptyList(),
    val bestTimeToVisit: String? = null,
    val recommendedTransport: String? = null,
    val members: List<MemberDto> = emptyList(),
    val totalExpense: Double? = null,
    val expenseSplits: List<ExpenseSplitDto> = emptyList(),
    val createdAt: String,
    val updatedAt: String,
)

@Serializable
data class MemberDto(
    val _id: String,
    val role: String,
    val user: TripUserDto,
)

@Serializable
data class TripUserDto(
    val _id: String,
    val email: String,
    val fullname: String,
    val profilePic: String? = null,
)

@Serializable
data class ItineraryDto(
    val _id: String,
    val day: Int,
    val plan: String,
)

@Serializable
data class HotelDto(
    val _id: String,
    val name: String,
    val priceRange: String? = null,
    val description: String? = null,
)

@Serializable
data class RestaurantDto(
    val _id: String,
    val name: String,
    val specialty: String? = null,
    val location: String? = null,
)

@Serializable
data class ExpenseSplitDto(
    val totalExpense: Double? = null,
    val perPerson: Double? = null,
    val contributions: List<ContributionDto> = emptyList(),
    val balances: List<BalanceDto> = emptyList(),
    val settlements: List<SettlementDto> = emptyList(),
)

@Serializable
data class ContributionDto(
    val user: String,
    val amount: Double,
)

@Serializable
data class BalanceDto(
    val user: String,
    val amount: Double,
)

@Serializable
data class SettlementDto(
    val from: String,
    val to: String,
    val amount: Double,
)
