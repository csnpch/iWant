package com.example.iwant.Maps


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


class PickupLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var pickup_location_btn_confirm_location: LinearLayout

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var latLngChooseLocation = DoubleArray(2)


    private fun init(savedInstanceState: Bundle?) {

        pickup_location_btn_confirm_location = findViewById(R.id.pickup_location_btn_confirm_location)
        pickup_location_btn_confirm_location.setOnClickListener{
            val returnIntent = Intent()
            returnIntent.putExtra("latLngChooseLocation", "lat:${latLngChooseLocation[0]},lng:${latLngChooseLocation[1]}")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        mapView = findViewById(R.id.pickup_location_googleMapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

    }


    private fun getLatLngChooseLocationFromAddWish() {
        // Retrieve the latitude and longitude values from the Intent
        val latLngString = intent.getStringExtra("latLngChooseLocation")
        val latLngParts = latLngString!!.split(",")
        if (latLngParts.isNotEmpty()) {
            latLngChooseLocation[0] = latLngParts[0].substring(4).toDouble()
            latLngChooseLocation[1] = latLngParts[1].substring(4).toDouble()
            Toast.makeText(this, "Data from Previous Activity: lat = ${latLngChooseLocation[0]}, log = ${latLngChooseLocation[1]}", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "Can't get latLng from AddWish", Toast.LENGTH_SHORT).show()
        }

    }



    override fun onMapReady(googleMap: GoogleMap) {

        PermissionUtils.checkLocationPermission(this);
        this.getLatLngChooseLocationFromAddWish()

        this.googleMap = googleMap
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latLngChooseLocation[0], latLngChooseLocation[1]), 19f))

        // Create marker at center of map
        val markerOptions = MarkerOptions()
            .position(this.googleMap.cameraPosition.target)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        val marker = this.googleMap.addMarker(markerOptions)

        // Update marker position as map moves
        this.googleMap.setOnCameraMoveListener {
            val latlng = this.googleMap.cameraPosition.target
            marker?.position = latlng
            latLngChooseLocation[0] = latlng.latitude
            latLngChooseLocation[1] = latlng.longitude
        }

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