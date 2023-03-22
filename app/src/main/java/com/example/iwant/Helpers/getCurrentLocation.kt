package com.example.iwant.Helpers

import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


fun getCurrentLocation(activity: Activity, callback: (Pair<Double, Double>) -> Unit) {
    // Check if location permissions are granted
    if (ActivityCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != android.content.pm.PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != android.content.pm.PackageManager.PERMISSION_GRANTED
    ) {
        // Location permissions not granted, return null
        return
    }

    // Get the location manager
    val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // Check if the device has a location provider
    if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
        !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    ) {
        // Location provider disabled, prompt user to turn it on
        return
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
    if (location != null) {
        val currentLocation = Pair(location.latitude, location.longitude)
        callback(currentLocation)
    }
}



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
