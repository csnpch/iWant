package com.example.iwant.Wish

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.example.iwant.R
import com.google.android.flexbox.FlexboxLayout


class AddWishActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var btn_pickup_location: FlexboxLayout
    lateinit var txt_title: TextView
    lateinit var txt_desciption: TextView
    lateinit var txt_benefit: TextView
    lateinit var txt_contact: TextView
    lateinit var btn_create_wish: LinearLayout
    lateinit var btn_cancel: LinearLayout


    private fun init() {

        txt_title = findViewById(R.id.add_wish_txt_title)
        txt_desciption = findViewById(R.id.add_wish_txt_description)
        txt_benefit = findViewById(R.id.add_wish_txt_benefit)
        txt_contact = findViewById(R.id.add_wish_txt_contact)

        btn_pickup_location = findViewById(R.id.add_wish_btn_pickup_location)
        btn_pickup_location.setOnClickListener(this)

        btn_create_wish = findViewById(R.id.add_wish_btn_create_wish)
        btn_create_wish.setOnClickListener(this)

        btn_cancel = findViewById(R.id.add_wish_btn_cancel)
        btn_cancel.setOnClickListener(this)

    }


    override fun onClick(v: View?) {

        when (v?.id) {

            btn_pickup_location.id -> {
                startActivity(Intent(this, PickupLocationWishActivity::class.java))
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

        this.init()
    }


    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.no_change,R.anim.slide_right)
    }

}