package com.example.iwant.Helpers

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat

fun getCurrentLocation(context: Context): Pair<Double, Double>? {
    // Check if location permissions are granted
    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != android.content.pm.PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != android.content.pm.PackageManager.PERMISSION_GRANTED
    ) {
        // Location permissions not granted, return null
        return null
    }

    // Get the location manager
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // Check if the device has a location provider
    if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
        !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    ) {
        // Location provider disabled, prompt user to turn it on
        return null
    }

    // Get the last known location from the network provider
    val networkLocation: Location? =
        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

    // Get the last known location from the GPS provider
    val gpsLocation: Location? =
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

    // Choose the more recent of the two locations
    val location: Location? = if (networkLocation?.time ?: 0 > gpsLocation?.time ?: 0) networkLocation else gpsLocation

    // If we have a location, return the latitude and longitude
    return if (location != null) Pair(location.latitude, location.longitude) else null
}
