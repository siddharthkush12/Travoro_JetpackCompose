package com.travoro.app.data.remote.dto.trips

import kotlinx.serialization.Serializable

@Serializable
data class BillSplitResponse(
    val `data`: BillSplitData?,
    val message: String?,
    val success: Boolean?,
)

@Serializable
data class BillSplitData(
    val totalExpense: Int,
    val perPerson: Int,
    val contributions: List<ContributionDto> = emptyList(),
    val settlements: List<SettlementDto?>? = emptyList(),
    val createdAt: String? = null,
)
