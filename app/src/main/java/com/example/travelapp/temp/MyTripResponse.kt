package com.example.travelapp.temp


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyTripResponse(
    @SerialName("data")
    val `data`: List<Data?>?,
    @SerialName("message")
    val message: String?,
    @SerialName("success")
    val success: Boolean?
)



@Serializable
data class Data(
    @SerialName("balances")
    val balances: List<Balance?>?,
    @SerialName("bestTimeToVisit")
    val bestTimeToVisit: String?,
    @SerialName("budget")
    val budget: Int?,
    @SerialName("budgetBreakdown")
    val budgetBreakdown: BudgetBreakdown?,
    @SerialName("coverImage")
    val coverImage: String?,
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("createdBy")
    val createdBy: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("destination")
    val destination: String?,
    @SerialName("hotels")
    val hotels: List<Hotel?>?,
    @SerialName("_id")
    val id: String?,
    @SerialName("images")
    val images: List<String?>?,
    @SerialName("itinerary")
    val itinerary: List<Itinerary?>?,
    @SerialName("latitude")
    val latitude: Double?,
    @SerialName("longitude")
    val longitude: Double?,
    @SerialName("members")
    val members: List<Member?>?,
    @SerialName("plannerType")
    val plannerType: String?,
    @SerialName("recommendedTransport")
    val recommendedTransport: String?,
    @SerialName("restaurants")
    val restaurants: List<Restaurant?>?,
    @SerialName("status")
    val status: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("totalExpense")
    val totalExpense: Int?,
    @SerialName("travelStyle")
    val travelStyle: String?,
    @SerialName("travelTips")
    val travelTips: List<String?>?,
    @SerialName("tripType")
    val tripType: String?,
    @SerialName("updatedAt")
    val updatedAt: String?,
    @SerialName("__v")
    val v: Int?
)



@Serializable
data class Balance(
    @SerialName("user")
    val user: String?,

    @SerialName("amount")
    val amount: Int?
)


@Serializable
data class BudgetBreakdown(
    @SerialName("accommodation")
    val accommodation: String?,
    @SerialName("activities")
    val activities: String?,
    @SerialName("food")
    val food: String?,
    @SerialName("transport")
    val transport: String?
)




@Serializable
data class Hotel(
    @SerialName("description")
    val description: String?,
    @SerialName("_id")
    val id: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("priceRange")
    val priceRange: String?
)


@Serializable
data class Itinerary(
    @SerialName("day")
    val day: Int?,
    @SerialName("_id")
    val id: String?,
    @SerialName("plan")
    val plan: String?
)





@Serializable
data class Member(
    @SerialName("_id")
    val id: String?,
    @SerialName("role")
    val role: String?,
    @SerialName("user")
    val user: User?
)



@Serializable
data class Restaurant(
    @SerialName("_id")
    val id: String?,
    @SerialName("location")
    val location: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("specialty")
    val specialty: String?
)





@Serializable
data class User(
    @SerialName("email")
    val email: String?,
    @SerialName("_id")
    val id: String?
)







