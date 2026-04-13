package com.travoro.app.ui.home.navigationDrawer.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travoro.app.core.network.ApiResult
import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.data.remote.dto.suggestion.Data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
    @Inject
    constructor(
        private val travelApiService: TravelApiService,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<NotificationState>(NotificationState.Idle)
        val uiState = _uiState.asStateFlow()

        private val _suggestions = MutableStateFlow<List<Data>>(emptyList())
        val suggestions = _suggestions.asStateFlow()

        fun fetchNotification() {
            viewModelScope.launch {
                _uiState.value = NotificationState.Loading
                val response =
                    safeApiCall {
                        travelApiService.getAllSuggestions()
                    }
                when (response) {
                    is ApiResult.Success -> {
                        val list = response.data.data?.filterNotNull() ?: emptyList()
                        _suggestions.value = list
                        _uiState.value = NotificationState.Success(list)
                    }

                    is ApiResult.Error -> {
                        _uiState.value = NotificationState.Error(response.message)
                    }

                    is ApiResult.Exception -> {
                        _uiState.value = NotificationState.Error(response.message)
                    }
                }
            }
        }

        sealed class NotificationState {
            object Idle : NotificationState()

            object Loading : NotificationState()

            data class Success(val suggestions: List<Data>) : NotificationState()

            data class Error(val message: String) : NotificationState()
        }
    }
