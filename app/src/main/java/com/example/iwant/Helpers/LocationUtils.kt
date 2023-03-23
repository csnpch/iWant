package com.example.iwant.Helpers

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*

private const val INTERVAL: Long = 1000
private const val FASTEST_INTERVAL: Long = 240000

fun getCurrentLocation(fragment: Fragment, onSuccess: (Pair<Double, Double>) -> Unit) {
    // Create a location request
    val locationRequest = LocationRequest.create().apply {
        interval = INTERVAL
        fastestInterval = FASTEST_INTERVAL
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    // Create a location callback
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult ?: return
            val lastLocation = locationResult.lastLocation
            onSuccess(Pair(lastLocation.latitude, lastLocation.longitude))
        }
    }

    // Check if location permissions are granted
    if (ActivityCompat.checkSelfPermission(
            fragment.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            fragment.requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Location permissions not granted, request them
        ActivityCompat.requestPermissions(
            fragment.requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            fragment.activity?.applicationInfo?.uid ?: 0
        )
        return
    }

    // Get the fused location provider client
    val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(fragment.requireContext())

    // Request location updates
    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )

    // Get the last known location
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            onSuccess(Pair(location.latitude, location.longitude))
        }
    }
}
