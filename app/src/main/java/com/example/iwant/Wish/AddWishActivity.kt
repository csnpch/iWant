package com.example.iwant.Wish

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.example.iwant.Helpers.getCurrentLocation
import com.example.iwant.R
import com.google.android.flexbox.FlexboxLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng


class AddWishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var txt_name_location: TextView
    private lateinit var btn_pickup_location: FlexboxLayout
    private lateinit var txt_title: TextView
    private lateinit var txt_desciption: TextView
    private lateinit var txt_benefit: TextView
    private lateinit var txt_contact: TextView
    private lateinit var btn_create_wish: LinearLayout
    private lateinit var btn_cancel: LinearLayout
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var latLngChooseLocation = DoubleArray(2)


    private fun init() {

        txt_title = findViewById(R.id.add_wish_txt_title)
        txt_desciption = findViewById(R.id.add_wish_txt_description)
        txt_benefit = findViewById(R.id.add_wish_txt_benefit)
        txt_contact = findViewById(R.id.add_wish_txt_contact)

        txt_name_location = findViewById(R.id.add_wish_txt_name_location);
        txt_name_location.text = "Current location" // find name location by latlng, if don't have name show lat long value = "14.25652..., 7.13454..."

        btn_pickup_location = findViewById(R.id.add_wish_btn_pickup_location)
        btn_pickup_location.setOnClickListener(this)

        btn_create_wish = findViewById(R.id.add_wish_btn_create_wish)
        btn_create_wish.setOnClickListener(this)

        btn_cancel = findViewById(R.id.add_wish_btn_cancel)
        btn_cancel.setOnClickListener(this)

    }


    private fun receiveValueFromPickupLocation() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val returnedValue = data?.getStringExtra("latLngChooseLocation")
                if (returnedValue != "") {
                    val latLngParts = returnedValue!!.split(",")
                    if (latLngParts.isNotEmpty()) {
                        latLngChooseLocation[0] = latLngParts[0].substring(4).toDouble()
                        latLngChooseLocation[1] = latLngParts[1].substring(4).toDouble()
                        Toast.makeText(this, "AddWish: lat = ${latLngChooseLocation[0]}, log = ${latLngChooseLocation[1]}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Can't get latLng from pickupLocation", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun getLatLngLocationDefault() {

        var statusOnUpdate = false
        if (statusOnUpdate) {
            // set default from old data before update
            latLngChooseLocation[0] = 14.150816666666667
            latLngChooseLocation[1] = 101.36116666666666
        } else {
            getCurrentLocation(this) { location ->
                latLngChooseLocation[0] = location.first
                latLngChooseLocation[1] = location.second
            }
        }

    }


    override fun onClick(v: View?) {

        when (v?.id) {

            btn_pickup_location.id -> {
                val intent = Intent(this, PickupLocationAddWishActivity::class.java)
                intent.putExtra("latLngChooseLocation", "lat:${latLngChooseLocation[0]},lng:${latLngChooseLocation[1]}")
                activityResultLauncher.launch(intent)
                overridePendingTransition(R.anim.slide_left,R.anim.no_change)
            }
            btn_create_wish.id -> {
                Toast.makeText(this, "onClick create wish", Toast.LENGTH_SHORT).show()
            }
            btn_cancel.id -> finish()

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wish)

        // Custom Appbar
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.shape_rounded_appbar))
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.appbar_layout)

        this.getLatLngLocationDefault();
        this.receiveValueFromPickupLocation()
        this.init()
    }


    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.no_change,R.anim.slide_right)
    }

}