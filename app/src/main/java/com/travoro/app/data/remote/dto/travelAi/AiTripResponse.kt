package com.travoro.app.data.remote.dto.travelAi

data class AiTripResponse(
    val success: Boolean,
    val message: String,
    val data: AiTripData,
)

data class AiTripData(
    val title: String,
    val description: String,
    val destination: String,
    val latitude: Double,
    val longitude: Double,
    val budget: Int,
    val days: Int,
    val startDate: String,
    val endDate: String,
    val travelStyle: String,
    val groupType: String,
    val coverImage: String,
    val images: List<String>,
    val itinerary: List<Itinerary>,
    val hotels: List<Hotel>,
    val restaurants: List<Restaurant>,
    val budgetBreakdown: BudgetBreakdown,
    val travelTips: List<String>,
    val bestTimeToVisit: String,
    val recommendedTransport: String,
)

data class Itinerary(
    val day: Int,
    val plan: String,
)

data class Hotel(
    val name: String,
    val priceRange: String,
    val description: String,
)

data class Restaurant(
    val name: String,
    val specialty: String,
    val location: String,
)

data class BudgetBreakdown(
    val transport: String,
    val accommodation: String,
    val food: String,
    val activities: String,
)
