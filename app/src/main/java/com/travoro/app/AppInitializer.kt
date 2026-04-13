package com.travoro.app

import com.google.firebase.messaging.FirebaseMessaging
import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.di.Session
import com.travoro.app.di.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitializer @Inject constructor(
    private val session: Session,
    private val travelApiService: TravelApiService,
) {
    fun initializeApp(scope: CoroutineScope) {
        connectSocket()
        sendFcmToken(scope)
    }
    private fun connectSocket() {
        session.getToken()?.let { token ->
            SocketManager.connect(token)
        }
    }
    private fun sendFcmToken(scope: CoroutineScope) {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            scope.launch {
                safeApiCall {
                    travelApiService.saveToken(
                        mapOf("token" to token),
                    )
                }
            }
        }
    }
}
