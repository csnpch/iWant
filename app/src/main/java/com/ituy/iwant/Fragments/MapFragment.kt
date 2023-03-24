package com.ituy.iwant.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.ituy.iwant.Helpers.PermissionUtils
import com.ituy.iwant.Helpers.getCurrentLocation
import com.ituy.iwant.R
import com.ituy.iwant.Wishs.AddWishActivity
import com.google.android.flexbox.FlexboxLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ituy.iwant.Helpers.Helpers
import com.ituy.iwant.Maps.PickupLocationActivity


class MapFragment : Fragment(), OnMapReadyCallback, View.OnClickListener {


    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var txt_your_location: TextView
    private lateinit var btn_location_choose: FlexboxLayout
    private lateinit var btn_floating_action_button: FloatingActionButton

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var currentUserLocation = DoubleArray(2)


    private fun initView(view: View, savedInstanceState: Bundle?): View {
        txt_your_location = view.findViewById(R.id.map_txt_your_location)

        btn_location_choose = view.findViewById(R.id.map_btn_location_choose_info)
        btn_location_choose.setOnClickListener(this)

        btn_floating_action_button = view.findViewById(R.id.map_btn_floating_action_button)
        btn_floating_action_button.setOnClickListener(this)

        mapView = view.findViewById(R.id.map_googleMapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view;
    }


    private fun getUserLocation() {
        getCurrentLocation(requireActivity()) { location ->
            currentUserLocation[0] = location.first!!
            currentUserLocation[1] = location.second!!
            println("Current Location is ${currentUserLocation[0]}:${currentUserLocation[1]}")

            // before get current user location next move camera to location
            val currentLocation = LatLng(currentUserLocation[0], currentUserLocation[1])
            this@MapFragment.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14f))
        }
    }


    private fun showPickupLocationName() {
        // Check if view only not move location
        if (currentUserLocation[0] == currentUserLocation[0] && currentUserLocation[1] == currentUserLocation[1]) {
            txt_your_location.text = "Current location"
            return;
        }

        val helpers = Helpers()
        val strLat = helpers.subStringLength(currentUserLocation[0].toString(), 12)
        val strLng = helpers.subStringLength(currentUserLocation[1].toString(), 12)
        txt_your_location.text = "$strLat, $strLng"
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            btn_floating_action_button.id -> {
                startActivity(Intent(requireContext(), AddWishActivity::class.java))
                requireActivity().overridePendingTransition(R.anim.slide_left,R.anim.no_change)
            }
            btn_location_choose.id -> {
                val intent = Intent(requireContext(), PickupLocationActivity::class.java)
                intent.putExtra("latLngChooseLocation", "lat:${currentUserLocation[0]},lng:${currentUserLocation[1]}")
                activityResultLauncher.launch(intent)
                requireActivity().overridePendingTransition(R.anim.no_change, R.anim.no_change)
            }
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


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val returnedValueLatLng = data?.getStringExtra("latLngChooseLocation")
                if (returnedValueLatLng != null) {
                    val latLng = returnedValueLatLng!!.split(",")
                    if (latLng.isNotEmpty()) {
                        currentUserLocation[0] = latLng[0].substring(4).toDouble()
                        currentUserLocation[1] = latLng[1].substring(4).toDouble()
                        this.showPickupLocationName()
                        Toast.makeText(requireContext(), "INTENT: lat = ${currentUserLocation[0]}, log = ${currentUserLocation[1]}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Can't get latLng from previous activity", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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