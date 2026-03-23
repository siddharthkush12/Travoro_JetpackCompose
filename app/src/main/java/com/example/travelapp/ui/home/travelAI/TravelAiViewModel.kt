package com.example.travelapp.ui.home.travelAI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.core.network.ApiResult
import com.example.travelapp.core.network.safeApiCall
import com.example.travelapp.data.remote.api.TravelApiService
import com.example.travelapp.data.remote.dto.travelAi.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelAiViewModel @Inject constructor(
    private val travelApi: TravelApiService
) : ViewModel() {

    /* ---------------- UI STATE ---------------- */

    private val _uiState = MutableStateFlow<TravelAiEvent>(TravelAiEvent.Idle)
    val uiState = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<AiChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<TravelAiNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()


    /* ---------------- CHAT STATE ---------------- */

    private var step = 0

    private var budget = ""
    private var days = ""
    private var travelStyle = ""
    private var groupType = ""
    private var currentCity = ""


    init {
        startConversation()
    }

    /* ---------------- START CHAT ---------------- */

    private fun startConversation() {
        step = 0
        _uiState.value = TravelAiEvent.Idle
        _messages.value = listOf(

            AiChatMessage(text = "What is your total trip budget?") // No options, waiting for text
        )
    }


    /* ---------------- USER INPUT SUBMITTED ---------------- */

    fun onOptionSelected(textInput: String) {
        // Handle post-generation action buttons
        when(textInput) {
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
                isUser = true
            )
        )

        // Process the steps based on free-text input
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
                _messages.value = list
                generateTrip()
                return
            }
        }

        _messages.value = list
    }


    /* ---------------- GENERATE AI TRIP ---------------- */

    private fun generateTrip() {
        viewModelScope.launch {
            _uiState.value = TravelAiEvent.Loading

            val loadingList = _messages.value.toMutableList()
            loadingList.add(AiChatMessage(text = "🤖 Generating your trip..."))
            _messages.value = loadingList

            // Strip out any text from numbers (e.g., if user typed "5000 rs", it becomes "5000")
            val cleanBudget = budget.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
            val cleanDays = days.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0

            val response = safeApiCall {
                travelApi.generateAiTrip(
                    AiTripRequest(
                        budget = cleanBudget,
                        days = cleanDays,
                        travelStyle = travelStyle,
                        groupType = groupType,
                        currentCity = currentCity
                    )
                )
            }

            when (response) {
                is ApiResult.Success -> {
                    val list = _messages.value.toMutableList()
                    list.add(AiChatMessage(tripResult = response.data.data))

                    // I kept these two as buttons because they are Actions, not answers.
                    list.add(
                        AiChatMessage(
                            text = "Would you like another plan?",
                            options = listOf("Regenerate Trip", "Restart Trip")
                        )
                    )
                    _messages.value = list
                    _uiState.value = TravelAiEvent.Success
                }
                is ApiResult.Error -> resetFlowWithError(response.message)
                is ApiResult.Exception -> resetFlowWithError(response.message)
            }
        }
    }


    /* ---------------- ACCEPT TRIP ---------------- */

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

                    list.add(AiChatMessage(text = "✅ Trip saved successfully!"))
                    _messages.value = list
                    _uiState.value = TravelAiEvent.Success
                    _navigationEvent.emit(TravelAiNavigation.NavigateToAddMembers(tripId))
                }
                is ApiResult.Error -> _uiState.value = TravelAiEvent.Error(response.message)
                is ApiResult.Exception -> _uiState.value = TravelAiEvent.Error(response.message)
            }
        }
    }


    /* ---------------- RESET FLOW ---------------- */

    private fun resetFlow() {
        step = 0
        budget = ""
        days = ""
        travelStyle = ""
        groupType = ""
        currentCity = ""
        startConversation()
    }

    private fun resetFlowWithError(message: String) {
        step = 0
        _uiState.value = TravelAiEvent.Idle
        _messages.value = listOf(
            AiChatMessage(text = "⚠ $message"),
            AiChatMessage(text = "Let's try again. What is your total trip budget?")
        )
    }

    /* ---------------- EVENTS ---------------- */

    sealed class TravelAiNavigation {
        data class NavigateToAddMembers(val tripId: String) : TravelAiNavigation()
    }

    sealed class TravelAiEvent {
        object Idle : TravelAiEvent()
        object Loading : TravelAiEvent()
        object Success : TravelAiEvent()
        data class Error(val message: String) : TravelAiEvent()
    }
}