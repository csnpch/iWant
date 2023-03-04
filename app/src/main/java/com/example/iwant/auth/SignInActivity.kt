package com.example.iwant.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iwant.R

class SignInActivity : AppCompatActivity() {


    private fun init() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()

        this.init()
    }


}