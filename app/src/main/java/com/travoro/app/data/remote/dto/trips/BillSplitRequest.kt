package com.travoro.app.data.remote.dto.trips

import kotlinx.serialization.Serializable

@Serializable
data class BillSplitRequest(
    val payments: List<Payment?>?,
)

@Serializable
data class Payment(
    val amount: Int,
    val user: String?,
)
