package com.example.iwant.Auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.iwant.R

class TermsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btn_continue: LinearLayout
    private lateinit var btn_back: ImageView
    private lateinit var btn_read_terms_of_services: TextView

    private fun init() {
        btn_back = findViewById(R.id.term_btn_back)
        btn_back.setOnClickListener(this)

        btn_continue = findViewById(R.id.term_btn_continue)
        btn_continue.setOnClickListener(this)

        btn_read_terms_of_services = findViewById(R.id.term_btn_read_terms_of_services)
        btn_read_terms_of_services.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            btn_back.id -> finish()
            btn_continue.id -> println("continue")
            btn_read_terms_of_services.id -> println("read")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        supportActionBar?.hide()

        this.init()
    }


}