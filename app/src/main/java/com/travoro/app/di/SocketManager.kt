package com.travoro.app.di

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketManager {
    private var socket: Socket? = null

    // ---------------- CONNECT ----------------

    fun connect(token: String) {
        if (socket?.connected() == true) return

        val options =
            IO.Options().apply {
                auth = mapOf("token" to token)
                transports = arrayOf("websocket")
            }

        socket = IO.socket("https://travelappbackend-ayig.onrender.com", options)

        socket?.on(Socket.EVENT_CONNECT) {
            Log.d("Socket", "SOCKET CONNECTED")
        }

        socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e("Socket", "SOCKET ERROR: ${args.joinToString()}")
        }

        socket?.connect()
    }

    // ---------------- DISCONNECT ----------------

    fun disconnect() {
        socket?.disconnect()
        socket = null
    }

    // ---------------- JOIN GROUP ----------------

    fun joinGroup(groupId: String) {
        socket?.emit("join-group", groupId)

        Log.d("Socket", "Joined group: $groupId")
    }

    // ---------------- SEND MESSAGE ----------------

    fun sendMessage(
        groupId: String,
        senderId: String,
        text: String?,
        type: String = "text",
        mediaUrl: String? = null,
    ) {
        val json =
            JSONObject().apply {
                put("groupId", groupId)
                put("senderId", senderId)
                put("text", text)
                put("type", type)
                put("mediaUrl", mediaUrl)
            }

        socket?.emit("send-message", json)

        Log.d("Socket", "Message sent: $json")
    }

    // ---------------- RECEIVE MESSAGE ----------------

    fun listenMessages(onMessageReceived: (JSONObject) -> Unit) {
        socket?.off("receive-message")

        socket?.on("receive-message") { args ->

            if (args.isNotEmpty()) {
                val data = args[0] as? JSONObject ?: return@on

                Log.d("Socket", "Message received: $data")

                onMessageReceived(data)
            }
        }
    }

    fun joinTripLocation(tripId: String) {
        socket?.emit(
            "join-trip-location",
            tripId,
        )

        Log.d("Socket", "Joined trip room: $tripId")
    }

    fun sendLocation(
        tripId: String,
        userId: String,
        latitude: Double,
        longitude: Double,
    ) {
        val json =
            JSONObject().apply {
                put("tripId", tripId)

                put("userId", userId)

                put("latitude", latitude)

                put("longitude", longitude)
            }

        socket?.emit(
            "send-location",
            json,
        )
    }

    fun listenLocations(onLocation: (JSONObject) -> Unit) {
        socket?.off("receive-location")

        socket?.on("receive-location") { args ->

            val data = args[0] as JSONObject

            Log.d("Socket", "Location update: $data")

            onLocation(data)
        }
    }
}
