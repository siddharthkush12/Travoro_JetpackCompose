package com.travoro.app.ui.utils.mapNavigation

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun openGoogleMapRoute(
    context: Context,
    sourceLat: Double,
    sourceLng: Double,
    destinationLat: Double,
    destinationLng: Double,
) {
    val uri =
        ("https://www.google.com/maps/dir/?api=1&origin=$sourceLat,$sourceLng&destination=$destinationLat,$destinationLng&travelmode=driving").toUri()

    val intent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(intent)
}

fun openGoogleMapsCurrentLocation(
    context: Context,
    lat: Double,
    lng: Double,
) {
    val uri = "geo:$lat,$lng?q=$lat,$lng(Current Location)".toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    context.startActivity(intent)
}
