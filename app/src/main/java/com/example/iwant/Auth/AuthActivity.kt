package com.example.iwant.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.iwant.MainActivity
import com.example.iwant.R
import com.google.android.flexbox.FlexboxLayout

class AuthActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var auth_btnSignInLine: FlexboxLayout
    private lateinit var auth_btnSignInGoogle: FlexboxLayout


    private fun init() {
        auth_btnSignInLine = findViewById(R.id.auth_btnSignInLine)
        auth_btnSignInLine.setOnClickListener(this)

        auth_btnSignInGoogle = findViewById(R.id.auth_btnSignInGoogle)
        auth_btnSignInGoogle.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            auth_btnSignInLine.id ->
                startActivity(Intent(this@AuthActivity, TermsActivity::class.java))
            auth_btnSignInGoogle.id ->
                startActivity(Intent(this@AuthActivity, TermsActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportActionBar?.hide()

        this.init()
    }


}