package com.example.iwant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {


    private fun setAppbar() {
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_appbar))
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.appbar_layout)
    }


    private fun init() {
        // For declare xml to variable or others
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val statusAuth = true
        when (statusAuth) {
            true -> this.setAppbar()            // Show appbar
            false -> supportActionBar?.hide()    // Hide appbar for login & register screen
        }

        setContentView(R.layout.activity_main)
        this.init()
    }


}