package com.example.iwant.Helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat

fun getCurrentLocation(context: Context): Pair<Double, Double>? {
    // Check if location permissions are granted
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
        // Location permissions not granted, return null
        return null
    }

    // Get the location manager
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // Get the last known location from the network provider
    val networkLocation: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

    // Get the last known location from the GPS provider
    val gpsLocation: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

    // Choose the more recent of the two locations
    val location: Location? = if (networkLocation?.time ?: 0 > gpsLocation?.time ?: 0) networkLocation else gpsLocation

    // If we have a location, return the latitude and longitude
    return if (location != null) Pair(location.latitude, location.longitude) else null
}
