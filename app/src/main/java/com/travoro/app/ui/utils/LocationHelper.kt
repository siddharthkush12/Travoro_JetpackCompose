package com.travoro.app.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

@Suppress("DEPRECATION")
class LocationHelper(context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(priority: Int): Location? = suspendCancellableCoroutine { cont ->
        fusedLocationClient.getCurrentLocation(priority, null).addOnSuccessListener { location ->
            if (location != null && isFresh(location)) {
                cont.resume(location)
            } else {
                requestFreshLocation(cont)
            }
        }.addOnFailureListener {
            requestFreshLocation(cont)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestFreshLocation(cont: kotlin.coroutines.Continuation<Location?>) {
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000,
        ).setMinUpdateIntervalMillis(500).setMaxUpdates(1).build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                fusedLocationClient.removeLocationUpdates(this)
                cont.resume(
                    result.lastLocation,
                )
            }
        }
        fusedLocationClient.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper(),
        )
    }

    private fun isFresh(location: Location): Boolean {
        val age = System.currentTimeMillis() - location.time
        return age < 5000
    }

    fun getCityFromLocation(
        context: Context,
        latitude: Double,
        longitude: Double,
    ): String? {
        return try {
            val geocoder = Geocoder(
                context,
                Locale.getDefault(),
            )

            val addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                1,
            )
            addresses?.firstOrNull()?.locality ?: addresses?.firstOrNull()?.subAdminArea
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
