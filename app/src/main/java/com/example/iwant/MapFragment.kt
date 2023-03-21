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
import com.example.iwant.Helpers.PermissionUtils
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


    private fun initView(view: View, savedInstanceState: Bundle?): View {
        btn_location_choose = view.findViewById(R.id.map_btn_location_choose)
        txt_your_location = view.findViewById(R.id.map_txt_your_location)

        mapView = view.findViewById(R.id.map_googleMapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view;
    }


    private fun getUserLocation() {
        getCurrentLocation(this) { location ->
            currentUserLocation[0] = location.first
            currentUserLocation[1] = location.second

            val currentLocation = LatLng(currentUserLocation[0], currentUserLocation[1])
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14f))
            println("Current Location is $currentLocation")
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        PermissionUtils.checkLocationPermission(this@MapFragment);
        this.getUserLocation()
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