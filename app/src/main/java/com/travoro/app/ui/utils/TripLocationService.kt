package com.travoro.app.ui.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.travoro.app.R
import com.travoro.app.di.SocketManager
import kotlinx.coroutines.*

class TripLocationService : Service() {
    companion object {
        var isRunning = false
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var locationJob: Job? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        isRunning = true
        val tripId = intent?.getStringExtra("tripId") ?: return START_NOT_STICKY
        val userId = intent.getStringExtra("userId") ?: return START_NOT_STICKY

        startForeground(
            101,
            createNotification(),
        )

        val locationHelper = LocationHelper(applicationContext)
        SocketManager.joinTripLocation(tripId)

        locationJob?.cancel()

        locationJob = serviceScope.launch {
            while (isActive) {
                try {
                    val location = locationHelper.getCurrentLocation(
                        PRIORITY_HIGH_ACCURACY,
                    )
                    location?.let {
                        SocketManager.sendLocation(
                            tripId,
                            userId,
                            it.latitude,
                            it.longitude,
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(5000)
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        isRunning = false
        locationJob?.cancel()
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        val channelId = "trip_location_channel"

        val manager = getSystemService(
            NotificationManager::class.java,
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Trip Location Tracking",
                NotificationManager.IMPORTANCE_LOW,
            )
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(
            this,
            channelId,
        ).setContentTitle("Live Trip Active").setContentText("Sharing location with trip members")
            .setSmallIcon(R.drawable.ic_launcher_round).setOngoing(true).build()
    }
}
