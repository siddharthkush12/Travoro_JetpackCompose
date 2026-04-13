package com.travoro.app.ui.home.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travoro.app.core.network.ApiResult
import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.data.remote.dto.message.ChatGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatGroupViewModel
    @Inject
    constructor(
        private val travelApiService: TravelApiService,
    ) : ViewModel() {
        private val _groups = MutableStateFlow<List<ChatGroup>>(emptyList())
        val groups = _groups.asStateFlow()

        private val _uiState = MutableStateFlow<ChatGroupEvent>(ChatGroupEvent.Idle)
        val uiState = _uiState.asStateFlow()

        init {
            fetchGroups()
        }

        fun fetchGroups() {
            viewModelScope.launch {
                _uiState.value = ChatGroupEvent.Loading

                val response =
                    safeApiCall {
                        travelApiService.getChatGroup()
                    }
                when (response) {
                    is ApiResult.Success -> {
                        _groups.value = response.data.data
                        _uiState.value = ChatGroupEvent.Success
                    }

                    is ApiResult.Error -> {
                        _uiState.value =
                            ChatGroupEvent.Error(response.message)
                    }

                    is ApiResult.Exception -> {
                        _uiState.value =
                            ChatGroupEvent.Error(response.message)
                    }
                }
            }
        }

        sealed class ChatGroupEvent {
            object Idle : ChatGroupEvent()

            object Loading : ChatGroupEvent()

            object Success : ChatGroupEvent()

            data class Error(val message: String) : ChatGroupEvent()
        }
    }
