package com.example.iwant.Helpers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

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


// For Fragment
fun getCurrentLocation(fragment: Fragment, callback: (Pair<Double, Double>) -> Unit) {
    // Check if location permissions are granted
    if (ActivityCompat.checkSelfPermission(
            fragment.requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            fragment.requireContext(),
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Location permissions not granted, return null
        return
    }

    // Get the location manager
    val locationManager = fragment.requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

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
//fun getCurrentLocation(fragment: Fragment, onSuccess: (Pair<Double, Double>) -> Unit) {
//    // Create a location request
//    val locationRequest = LocationRequest.create().apply {
//        interval = INTERVAL
//        fastestInterval = FASTEST_INTERVAL
//        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//    }
//
//    // Create a location callback
//    val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            locationResult ?: return
//            val lastLocation = locationResult.lastLocation
//            onSuccess(Pair(lastLocation.latitude, lastLocation.longitude))
//        }
//    }
//
//    // Check if location permissions are granted
//    if (ActivityCompat.checkSelfPermission(
//            fragment.requireContext(),
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED &&
//        ActivityCompat.checkSelfPermission(
//            fragment.requireContext(),
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//        // Location permissions not granted, request them
//        ActivityCompat.requestPermissions(
//            fragment.requireActivity(),
//            arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ),
//            fragment.activity?.applicationInfo?.uid ?: 0
//        )
//        return
//    }
//
//    // Get the fused location provider client
//    val fusedLocationClient =
//        LocationServices.getFusedLocationProviderClient(fragment.requireContext())
//
//    // Request location updates
//    fusedLocationClient.requestLocationUpdates(
//        locationRequest,
//        locationCallback,
//        Looper.getMainLooper()
//    )
//
//    // Get the last known location
//    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//        if (location != null) {
//            onSuccess(Pair(location.latitude, location.longitude))
//        }
//    }
//}