package com.example.iwant.Helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionUtils {

    private const val CALL_PHONE_PERMISSION_REQUEST_CODE = 101
    private const val LOCATION_PERMISSION_REQUEST_CODE = 1

    fun requestCallPhonePermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) !=
            PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_PHONE_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    fun isCallPhonePermissionGranted(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }



    /**
     * Check if the app has been granted the location permission. If not, request the permission.
     *
     * @return true if the app has been granted the location permission, false otherwise
     */
    fun checkLocationPermission(activity: Activity): Boolean {
        return if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            false
        } else {
            // Permission is already granted
            true
        }
    }


    fun checkLocationPermission(fragment: Fragment): Boolean {
        return if (ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                fragment.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            false
        } else {
            // Permission is already granted
            true
        }
    }

    /**
     * Handle the result of a permission request. If the request is for the location permission and
     * it was granted, call the given callback.
     *
     * @param requestCode the request code passed to requestPermissions()
     * @param permissions the requested permissions
     * @param grantResults the grant results for the corresponding permissions
     * @param onLocationPermissionGranted callback to be called if the location permission was granted
     */
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        onLocationPermissionGranted: () -> Unit
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    onLocationPermissionGranted()
                }
            }
        }
    }

}


