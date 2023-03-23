package com.example.iwant.Map

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import com.example.iwant.Helpers.PermissionUtils
import com.example.iwant.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapViewActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var btn_close: LinearLayout

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var latLngChooseLocation = DoubleArray(2)


    private fun init(savedInstanceState: Bundle?) {

        btn_close = findViewById(R.id.mapview_btn_close)
        btn_close.setOnClickListener{
            finish()
        }

        mapView = findViewById(R.id.mapview_googleMapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

    }


    private fun getLatLngChooseLocationFromAddWish() {
        // Retrieve the latitude and longitude values from the Intent
        val latLngString = intent.getStringExtra("LatLng")
        val latLngParts = latLngString!!.split(",")
        if (latLngParts.isNotEmpty()) {
            latLngChooseLocation[0] = latLngParts[0].substring(4).toDouble()
            latLngChooseLocation[1] = latLngParts[1].substring(4).toDouble()
        } else {
            Toast.makeText(this, "Can't get latLng from Previous Activity", Toast.LENGTH_SHORT).show()
        }

    }



    override fun onMapReady(googleMap: GoogleMap) {

        PermissionUtils.checkLocationPermission(this);
        this.getLatLngChooseLocationFromAddWish()

        this.googleMap = googleMap
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latLngChooseLocation[0], latLngChooseLocation[1]), 17f))

        // Create marker at center of map
        val markerOptions = MarkerOptions()
            .position(this.googleMap.cameraPosition.target)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        this.googleMap.addMarker(markerOptions)

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
        setContentView(R.layout.activity_map_view)

        supportActionBar?.hide()
        this.init(savedInstanceState)
    }

}