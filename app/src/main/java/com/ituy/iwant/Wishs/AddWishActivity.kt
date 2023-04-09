package com.ituy.iwant.Wishs

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
import com.ituy.iwant.Helpers.Helpers
import com.ituy.iwant.Helpers.Validates
import com.ituy.iwant.Helpers.getCurrentLocation
import com.ituy.iwant.Maps.PickupLocationActivity
import com.ituy.iwant.R
import com.google.android.flexbox.FlexboxLayout
import com.ituy.iwant.Stores.LocalStore
import com.ituy.iwant.api.authentication.AuthenticationService
import com.ituy.iwant.api.wish.WishModel
import com.ituy.iwant.api.wish.WishService
import com.ituy.iwant.api.wish.dto.CreateWishRequest
import com.ituy.iwant.api.wish.dto.CreateWishResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddWishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var txt_name_location: TextView
    private lateinit var btn_pickup_location: FlexboxLayout
    private lateinit var txt_title: TextView
    private lateinit var txt_description: TextView
    private lateinit var txt_benefit: TextView
    private lateinit var txt_contact: TextView
    private lateinit var btn_create_wish: LinearLayout
    private lateinit var btn_cancel: LinearLayout
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var currentUserLocation = DoubleArray(2)
    private var latLngChooseLocation = DoubleArray(2)
    private var statusOnUpdate: Boolean = false
    private val apiService = WishService()

    private lateinit var profile: ArrayList<String>


    private fun init() {

        txt_title = findViewById(R.id.add_wish_txt_title)
        txt_description = findViewById(R.id.add_wish_txt_description)
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

        profile = LocalStore(this).getArrayList("profile", ArrayList())
        if (profile != null) {
            txt_contact.text = profile[1]
        }

    }


    private fun validateForm(): Boolean {
        val inputFields = listOf(
            "title" to txt_title,
            "contact" to txt_contact,
        )

        var statusValidate = true
        val validator = Validates()
        for (inputField in inputFields) {
            val key = inputField.first
            val editText = inputField.second

            val errorMessage = validator.input(key, editText.text.toString())
            if (errorMessage != null) {
                editText.error = errorMessage
                statusValidate = false
            }
        }
        return statusValidate;
    }


    private fun showPickupLocationName() {
        // Check if view only not move location
        if (currentUserLocation[0] == latLngChooseLocation[0] && currentUserLocation[1] == latLngChooseLocation[1]) {
            txt_name_location.text = "Current location"
            return;
        }

        val helpers = Helpers()
        val strLat = helpers.subStringLength(latLngChooseLocation[0].toString(), 12)
        val strLng = helpers.subStringLength(latLngChooseLocation[1].toString(), 12)
        txt_name_location.text = "$strLat, $strLng"
    }


    private fun receiveValueFromPickupLocation() {

        fun receiveLatLng(data: Intent?) {
            val returnedValueLatLng = data?.getStringExtra("latLngChooseLocation")
            if (returnedValueLatLng != "") {
                val latLng = returnedValueLatLng!!.split(",")
                if (latLng.isNotEmpty()) {
                    latLngChooseLocation[0] = latLng[0].substring(4).toDouble()
                    latLngChooseLocation[1] = latLng[1].substring(4).toDouble()
                    this.showPickupLocationName()
                    //  Toast.makeText(this, "INTENT: lat = ${latLngChooseLocation[0]}, log = ${latLngChooseLocation[1]}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Can't get latLng from previous activity", Toast.LENGTH_SHORT).show()
                }
            }
        }

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                receiveLatLng(data)
            }
        }
    }


    private fun getLatLngLocationDefault() {

        if (statusOnUpdate) {
            // set default from old data before update
            latLngChooseLocation[0] = 14.150816666666667
            latLngChooseLocation[1] = 101.36116666666666
        } else {
            getCurrentLocation(this) { location ->
                currentUserLocation[0] = location.first!!
                currentUserLocation[1] = location.second!!
                latLngChooseLocation[0] = currentUserLocation[0]
                latLngChooseLocation[1] = currentUserLocation[1]
            }
        }

    }


    private fun onCreateWish() {
        if (!this.validateForm()) return
        val token = LocalStore(this@AddWishActivity).getString("token", "")
        val call = apiService.createWish(token, CreateWishRequest(txt_title.text.toString(),
            (latLngChooseLocation[0].toString()+","+latLngChooseLocation[1].toString()),
            txt_description.text.toString(), txt_benefit.text.toString(),
            txt_contact.text.toString()))
        call.enqueue(object: Callback<CreateWishResponse> {
            override fun onResponse(
                call: Call<CreateWishResponse>,
                response: Response<CreateWishResponse>
            ) {
                if (response.body()?.status == true) {
                    finish()
                } else {
                    Toast.makeText(this@AddWishActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CreateWishResponse>, t: Throwable) {
                Toast.makeText(this@AddWishActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }


    override fun onClick(v: View?) {

        when (v?.id) {

            btn_pickup_location.id -> {
                val intent = Intent(this, PickupLocationActivity::class.java)
                intent.putExtra("latLngChooseLocation", "lat:${latLngChooseLocation[0]},lng:${latLngChooseLocation[1]}")
                activityResultLauncher.launch(intent)
                overridePendingTransition(R.anim.slide_left,R.anim.no_change)
            }
            btn_create_wish.id -> {
                this.onCreateWish()
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