package com.example.iwant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer

class SplashScreen : AppCompatActivity() {


    private fun validateAuthentication() {

        var statusAuth: Boolean = false
        when (statusAuth) {
            // Go to Home (Wish Page)
            true -> {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            }
            // Go to Auth Page
            false -> {
                startActivity(Intent(this@SplashScreen, AuthActivity::class.java))
            }
        }
        finish()

    }


    private fun main() {

        object: CountDownTimer(2500L, 1000L) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() {
                this@SplashScreen.validateAuthentication()
            }
        }.start()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        this.main();
    }


}