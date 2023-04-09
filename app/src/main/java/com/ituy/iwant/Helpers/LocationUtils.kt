package com.ituy.iwant.Helpers

import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat


//const val REQUEST_LOCATION_PERMISSION = 1001
//private const val INTERVAL: Long = 1000
//private const val FASTEST_INTERVAL: Long = 240000



// For activity
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


// Old for Fragment
//private fun getCurrentLocation(fragment: Fragment, onSuccess: (Pair<Double, Double>) -> Unit) {
//    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
//
//    if (ActivityCompat.checkSelfPermission(fragment.requireContext(), locationPermission) != PackageManager.PERMISSION_GRANTED) {
//        // Location permission not granted, request it
//        ActivityCompat.requestPermissions(fragment.requireActivity(), arrayOf(locationPermission), REQUEST_LOCATION_PERMISSION)
//        return
//    }
//
//    val locationManager = fragment.requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//        // No location provider is enabled, prompt the user to turn on location services
//        Toast.makeText(fragment.requireContext(), "Please turn on location services", Toast.LENGTH_LONG).show()
//        return
//    }
//
//    // Check the last known location from the network provider
//    var location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//
//    // Check the last known location from the GPS provider
//    if (location == null) {
//        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//    }
//
//    // If we don't have a last known location, request location updates
//    if (location == null) {
//        val locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                val currentLocation = Pair(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
//                onSuccess(currentLocation)
//
//                locationManager.removeUpdates(this)
//            }
//        }
//
//        val locationRequest = LocationRequest.create().apply {
//            interval = INTERVAL
//            fastestInterval = FASTEST_INTERVAL
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//
//        try {
//            locationManager.requestLocationUpdates(locationRequest, locationCallback, null)
//        } catch (e: SecurityException) {
//            // Handle exception
//            Log.e(TAG, "Exception: ${e.message}")
//        }
//    } else {
//        val currentLocation = Pair(location.latitude, location.longitude)
//        onSuccess(currentLocation)
//    }
//}