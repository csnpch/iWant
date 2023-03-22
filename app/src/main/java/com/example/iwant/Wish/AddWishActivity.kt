package com.example.iwant.Wish

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.example.iwant.R

class AddWishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wish)

        // Custom Appbar
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.shape_rounded_appbar))
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.appbar_layout)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.no_change,R.anim.slide_right)
        finish()
    }

}