package com.example.iwant

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.iwant.Helpers.getCurrentLocation
import com.google.android.flexbox.FlexboxLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng


class MapFragment : Fragment(), OnMapReadyCallback {


    private lateinit var btn_location_choose: FlexboxLayout
    private lateinit var txt_your_location: TextView
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var currentUserLocation = DoubleArray(2)
    private val LOCATION_PERMISSION_REQUEST_CODE = 1


    private fun initView(view: View, savedInstanceState: Bundle?): View {
        btn_location_choose = view.findViewById(R.id.map_btn_location_choose)
        txt_your_location = view.findViewById(R.id.map_txt_your_location)

        mapView = view.findViewById(R.id.map_googleMapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view;
    }



    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // Permission is already granted, get the user's location
            getUserLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                // If permission is granted, get the user's location
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getUserLocation()
                }
                return
            }
        }
    }

    private fun getUserLocation() {
        // Your code to get the user's location goes here
        // TEST : Get current location user from Helpers package
        val location: Pair<Double, Double>? = getCurrentLocation(requireContext())
        currentUserLocation[0] = location?.first ?: 0.0 // set default value to 0.0 if location is null
        currentUserLocation[1] = location?.second ?: 0.0 // set default value to 0.0 if location is null

        System.out.println("location : " + location)
    }



    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.checkLocationPermission()
        this.getUserLocation()

        // Test move location 1
        val thailandLocation = LatLng(13.7248904, 100.3521307)
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thailandLocation, 10f))

        // Test move location 2
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() {
                val currentLocation = LatLng(this@MapFragment.currentUserLocation[0], this@MapFragment.currentUserLocation[1])
                this@MapFragment.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10f))
            }
        }.start()
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
        arguments?.let {}
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_map, container, false)
        root = this.initView(root, savedInstanceState)

        this.main()
        return root
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply { }
            }
    }


    private fun main() {

    }


}