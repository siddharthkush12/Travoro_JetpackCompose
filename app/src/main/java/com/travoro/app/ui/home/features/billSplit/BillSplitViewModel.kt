package com.travoro.app.ui.home.features.billSplit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travoro.app.core.network.ApiResult
import com.travoro.app.data.remote.dto.trips.BillSplitData
import com.travoro.app.data.remote.dto.trips.BillSplitRequest
import com.travoro.app.data.remote.dto.trips.Payment
import com.travoro.app.data.remote.repository.BillSplitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillSplitViewModel @Inject constructor(
    private val repository: BillSplitRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<BillSplitState>(BillSplitState.Idle)
    val uiState = _uiState.asStateFlow()
    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments = _payments.asStateFlow()

    fun setExpense(
        userId: String,
        expense: Int,
    ) {
        _payments.value = _payments.value.map {
            if (it.user == userId) {
                it.copy(amount = expense)
            } else {
                it
            }
        }
    }

    fun setMembersWithContribution(
        members: List<String>,
        contributions: Map<String, Int>,
    ) {
        _payments.value = members.map {
            Payment(
                user = it,
                amount = contributions[it] ?: 0,
            )
        }
    }

    fun getBillSplit(tripId: String) {
        _uiState.value = BillSplitState.Loading
        viewModelScope.launch {
            val response = repository.getBillSplit(
                tripId = tripId,
                request = BillSplitRequest(
                    payments = _payments.value,
                ),
            )
            when (response) {
                is ApiResult.Success -> {
                    _uiState.value = BillSplitState.Success(response.data.data!!)
                }

                is ApiResult.Error -> {
                    _uiState.value = BillSplitState.Error(response.message)
                }

                is ApiResult.Exception -> {
                    _uiState.value = BillSplitState.Error(response.message)
                }
            }
        }
    }


    sealed class BillSplitState {
        object Idle : BillSplitState()
        object Loading : BillSplitState()
        data class Success(val data: BillSplitData) : BillSplitState()
        data class Error(val message: String) : BillSplitState()
    }
}
