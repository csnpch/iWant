package com.example.iwant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.iwant.auth.SignInActivity

class SplashScreen : AppCompatActivity() {


    private fun validateAuthentication() {

        var statusAuth: Boolean = true
        when (statusAuth) {
            // Go to Home (Wish Page)
            true -> {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            }
            // Go to Auth (SignIn Page)
            false -> {
                startActivity(Intent(this@SplashScreen, SignInActivity::class.java))
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