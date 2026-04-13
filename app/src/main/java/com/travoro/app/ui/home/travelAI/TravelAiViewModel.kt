package com.travoro.app.ui.home.travelAI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travoro.app.core.network.ApiResult
import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.data.remote.dto.travelAi.*
import com.travoro.app.ui.components.calculateEndDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelAiViewModel @Inject constructor(
    private val travelApi: TravelApiService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TravelAiEvent>(TravelAiEvent.Idle)
    val uiState = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<AiChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<TravelAiNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()


    private var step = 0

    private var budget = ""
    private var days = ""
    private var travelStyle = ""
    private var groupType = ""
    private var currentCity = ""
    private var startDate = ""

    init {
        startConversation()
    }


    private fun startConversation() {
        step = 0
        _uiState.value = TravelAiEvent.Idle
        _messages.value = listOf(
            AiChatMessage(text = "What is your total trip budget?"),
        )
    }


    fun onOptionSelected(textInput: String) {
        when (textInput) {
            "Restart Trip" -> {
                resetFlow()
                return
            }

            "Regenerate Trip" -> {
                generateTrip()
                return
            }
        }

        val list = _messages.value.toMutableList()
        list.add(
            AiChatMessage(
                text = textInput,
                isUser = true,
            ),
        )

        when (step) {
            0 -> {
                budget = textInput
                step = 1
                list.add(AiChatMessage(text = "How many days are you planning to travel?"))
            }

            1 -> {
                days = textInput
                step = 2
                list.add(AiChatMessage(text = "What travel style do you prefer? (e.g., Adventure, Relaxing, Luxury)"))
            }

            2 -> {
                travelStyle = textInput
                step = 3
                list.add(AiChatMessage(text = "Who are you travelling with? (e.g., Solo, Partner, Family)"))
            }

            3 -> {
                groupType = textInput
                step = 4
                list.add(AiChatMessage(text = "What is your starting city?"))
            }

            4 -> {
                currentCity = textInput
                step = 5

                list.add(
                    AiChatMessage(
                        text = "Select your trip start date",
                        options = listOf("Pick Date"),
                    ),
                )
            }

            5 -> {
                startDate = textInput
                list.add(
                    AiChatMessage(
                        text = "Start date selected: $startDate",
                    ),
                )
                _messages.value = list
                generateTrip()
                return
            }
        }

        _messages.value = list
    }


    private fun generateTrip() {
        viewModelScope.launch {
            _uiState.value = TravelAiEvent.Loading

            val loadingList = _messages.value.toMutableList()
            loadingList.add(AiChatMessage(text = " Generating your trip..."))
            _messages.value = loadingList

            val cleanBudget = budget.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
            val cleanDays = days.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0

            val response = safeApiCall {
                travelApi.generateAiTrip(
                    AiTripRequest(
                        budget = cleanBudget,
                        days = cleanDays,
                        travelStyle = travelStyle,
                        groupType = groupType,
                        currentCity = currentCity,
                        startDate = startDate,
                    ),
                )
            }

            when (response) {
                is ApiResult.Success -> {
                    val list = _messages.value.toMutableList()
                    list.add(AiChatMessage(tripResult = response.data.data))
                    list.add(
                        AiChatMessage(
                            text = "Would you like another plan?",
                            options = listOf("Regenerate Trip", "Restart Trip"),
                        ),
                    )
                    _messages.value = list
                    _uiState.value = TravelAiEvent.Success
                }

                is ApiResult.Error -> resetFlowWithError(response.message)
                is ApiResult.Exception -> resetFlowWithError(response.message)
            }
        }
    }

    fun acceptTrip(tripData: AiTripData) {
        viewModelScope.launch {
            _uiState.value = TravelAiEvent.Loading

            val response = safeApiCall {
                travelApi.acceptTrip(AcceptTripRequest(data = tripData))
            }

            when (response) {
                is ApiResult.Success -> {
                    val list = _messages.value.toMutableList()
                    val tripId = response.data.data.id

                    list.add(AiChatMessage(text = "Trip saved successfully!"))
                    _messages.value = list
                    _uiState.value = TravelAiEvent.Success
                    _navigationEvent.emit(
                        TravelAiNavigation.NavigateToAddMembers(
                            tripId, tripData.days
                        )
                    )
                }

                is ApiResult.Error -> {
                    if (response.message.contains("DATE_CONFLICT")) {
                        _uiState.value = TravelAiEvent.DateConflict(
                            tripData,
                        )
                    } else {
                        _uiState.value = TravelAiEvent.Error(
                            response.message,
                        )
                    }
                }

                is ApiResult.Exception -> _uiState.value = TravelAiEvent.Error(response.message)
            }
        }
    }

    fun updateTripDate(
        tripData: AiTripData,
        newStartDate: String,
    ) {
        viewModelScope.launch {
            val days = tripData.days

            val newEndDate = calculateEndDate(
                newStartDate,
                days,
            )

            val updatedTrip = tripData.copy(
                startDate = newStartDate,
                endDate = newEndDate,
            )

            acceptTrip(updatedTrip)
        }
    }

    private fun resetFlow() {
        step = 0
        budget = ""
        days = ""
        travelStyle = ""
        groupType = ""
        currentCity = ""
        startDate = ""
        startConversation()
    }

    private fun resetFlowWithError(message: String) {
        step = 0
        _uiState.value = TravelAiEvent.Idle
        _messages.value = listOf(
            AiChatMessage(text = "$message"),
            AiChatMessage(text = "Let's try again. What is your total trip budget?"),
        )
    }


    sealed class TravelAiNavigation {
        data class NavigateToAddMembers(val tripId: String, val days: Int) : TravelAiNavigation()
        object NavigateToHome : TravelAiNavigation()
    }

    sealed class TravelAiEvent {
        object Idle : TravelAiEvent()
        object Loading : TravelAiEvent()
        object Success : TravelAiEvent()
        data class Error(val message: String) : TravelAiEvent()
        data class DateConflict(val tripData: AiTripData) : TravelAiEvent()
    }
}
