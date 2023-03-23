package com.example.iwant.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.iwant.Helpers.PermissionUtils
import com.example.iwant.Helpers.getCurrentLocation
import com.example.iwant.R
import com.example.iwant.Wish.AddWishActivity
import com.google.android.flexbox.FlexboxLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MapFragment : Fragment(), OnMapReadyCallback {


    private lateinit var txt_your_location: TextView
    private lateinit var btn_location_choose: FlexboxLayout
    private lateinit var btn_floating_action_button: FloatingActionButton

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var currentUserLocation = DoubleArray(2)


    private fun initView(view: View, savedInstanceState: Bundle?): View {
        txt_your_location = view.findViewById(R.id.map_txt_your_location)
        btn_location_choose = view.findViewById(R.id.map_btn_location_choose)
        btn_floating_action_button = view.findViewById(R.id.map_btn_floating_action_button)

        btn_floating_action_button.setOnClickListener{
            startActivity(Intent(requireContext(), AddWishActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_left,R.anim.no_change)
        }

        mapView = view.findViewById(R.id.map_googleMapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view;
    }


    private fun getUserLocation() {
        getCurrentLocation(this) { location ->
            currentUserLocation[0] = location.first!!
            currentUserLocation[1] = location.second!!
            println("Current Location is ${currentUserLocation[0]}:${currentUserLocation[1]}")

            // before get current user location next move camera to location
            val currentLocation = LatLng(currentUserLocation[0], currentUserLocation[1])
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14f))
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