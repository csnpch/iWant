package com.example.iwant.Wish

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.iwant.Helpers.PermissionUtils
import com.example.iwant.Helpers.getCurrentLocation
import com.example.iwant.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class PickupLocationWishActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var pickup_location_btn_confirm_location: LinearLayout

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var currentUserLocation = DoubleArray(2)
    private var currentChooseLocation = DoubleArray(2)


    private fun init(savedInstanceState: Bundle?) {

        pickup_location_btn_confirm_location = findViewById(R.id.pickup_location_btn_confirm_location)
        pickup_location_btn_confirm_location.setOnClickListener{
            Toast.makeText(this, "lat = ${currentChooseLocation[0]}, long = ${currentChooseLocation[1]}", Toast.LENGTH_SHORT).show()
            finish()
        }

        mapView = findViewById(R.id.pickup_location_googleMapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

    }


    private fun getUserLocation() {
        getCurrentLocation(this) { location ->
            currentUserLocation[0] = location.first
            currentUserLocation[1] = location.second

            val currentLocation = LatLng(currentUserLocation[0], currentUserLocation[1])
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 19f))
            println("Current Location is $currentLocation")
            // find name location by latlng if don't have name show lat long value
        }
    }


    private fun setPosPickupLocation() {

        var statusOnUpdate = false

        // If update
        if (statusOnUpdate) {
            // get from old data before update
            currentChooseLocation[0] = 14.150816666666667
            currentChooseLocation[1] = 101.36116666666666

            val latlng = LatLng(currentChooseLocation[0],currentChooseLocation[1])
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 19f))
        }

        // Create marker at center of map
        val markerOptions = MarkerOptions()
            .position(this.googleMap.cameraPosition.target)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        val marker = this.googleMap.addMarker(markerOptions)

        // Update marker position as map moves
        this.googleMap.setOnCameraMoveListener {
            val latlng = this.googleMap.cameraPosition.target
            marker?.position = latlng
            currentChooseLocation[0] = latlng.latitude
            currentChooseLocation[1] = latlng.longitude
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        PermissionUtils.checkLocationPermission(this);
        this.getUserLocation()
        this.setPosPickupLocation();
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickup_location_wish)

        supportActionBar?.hide()
        this.init(savedInstanceState)
    }


}