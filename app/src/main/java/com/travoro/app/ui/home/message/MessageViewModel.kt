package com.travoro.app.ui.home.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travoro.app.core.network.ApiResult
import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.data.remote.dto.message.Message
import com.travoro.app.data.remote.dto.message.Sender
import com.travoro.app.di.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel
    @Inject
    constructor(
        private val travelApiService: TravelApiService,
    ) : ViewModel() {
        private val _messages = MutableStateFlow<List<Message>>(emptyList())
        val messages = _messages.asStateFlow()

        private val _text = MutableStateFlow("")
        val text = _text.asStateFlow()

        fun onTextChange(value: String) {
            _text.value = value
        }

        // ---------------- LOAD OLD MESSAGES ----------------

        fun loadMessages(groupId: String) {
            viewModelScope.launch {
                val response =
                    safeApiCall {
                        travelApiService.getMessages(groupId)
                    }

                when (response) {
                    is ApiResult.Success -> {
                        _messages.value = response.data.data
                    }

                    else -> {}
                }
            }
        }

        // ---------------- JOIN GROUP ----------------

        fun joinGroup(groupId: String) {
            SocketManager.joinGroup(groupId)
        }

        // ---------------- LISTEN REALTIME MESSAGES ----------------

        fun listenMessages() {
            SocketManager.listenMessages { data ->

                viewModelScope.launch {
                    val senderJson = data.optJSONObject("sender")

                    val message =
                        Message(
                            _id = data.optString("_id"),
                            chatGroup = data.optString("chatGroup"),
                            text = data.optString("text"),
                            type = data.optString("type"),
                            mediaUrl = data.optString("mediaUrl"),
                            createdAt = data.optString("createdAt"),
                            sender =
                                Sender(
                                    _id = senderJson?.optString("_id") ?: "",
                                    fullname = senderJson?.optString("fullname") ?: "",
                                    profilePic = senderJson?.optString("profilePic") ?: "",
                                ),
                        )

                    _messages.value = _messages.value + message
                }
            }
        }

        // ---------------- SEND MESSAGE ----------------

        fun sendMessage(
            groupId: String,
            userId: String,
        ) {
            if (_text.value.isBlank()) return

            SocketManager.sendMessage(
                groupId = groupId,
                senderId = userId,
                text = _text.value,
            )

            _text.value = ""
        }
    }
