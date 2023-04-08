package com.ituy.iwant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.ituy.iwant.Auths.AuthActivity
import com.ituy.iwant.Stores.LocalStore

class SplashScreen : AppCompatActivity() {


    private val timeDelay: Long = 2000


    private fun validateAuthentication() {

        val token = LocalStore(this).getString("token", "")
        // For dev need skip you can set statusAuth to true
        when (token.isNotEmpty()) {
            true -> {
                // Go to Home (Wish Page)
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            }
            false -> {
                // Go to Auth Page
                startActivity(Intent(this@SplashScreen, com.ituy.iwant.Auths.AuthActivity::class.java))
            }
        }
        // Then start activity finish current activity
        finish()

    }


    private fun init() {

        Thread(Runnable {
            Thread.sleep(timeDelay)
            Handler(Looper.getMainLooper()).post {
                this.validateAuthentication()
            }
        }).start()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        this.init();
    }


}