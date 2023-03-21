package com.example.iwant.Auth

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager.LayoutParams
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.iwant.MainActivity
import com.example.iwant.R


class TermsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btn_back: ImageView
    private lateinit var cb_acceptTermsOfServices: CheckBox
    private lateinit var btn_readTermsOfServices: TextView
    private lateinit var btn_continue: LinearLayout
    private lateinit var dialogTerms: AlertDialog

    private lateinit var edt_phoneNumber: EditText
    private var statusAcceptTerms: Boolean = false


    private fun init() {
        btn_back = findViewById(R.id.term_btn_back)
        btn_back.setOnClickListener(this)

        cb_acceptTermsOfServices = findViewById(R.id.terms_cb_accept)
        cb_acceptTermsOfServices.setOnCheckedChangeListener {
            _, isChecked ->
                this.statusAcceptTerms = isChecked
                this.toggleColorValidateAcceptTerms()
        }

        btn_readTermsOfServices = findViewById(R.id.terms_btn_read_terms_of_services)
        btn_readTermsOfServices.setOnClickListener(this)

        btn_continue = findViewById(R.id.terms_btn_continue)
        btn_continue.setOnClickListener(this)

        edt_phoneNumber = findViewById(R.id.terms_edt_phoneNumber)

        this.initDialogReadTermsOfServices()
    }


    private fun initDialogReadTermsOfServices() {
        val mBuilder: AlertDialog.Builder = AlertDialog.Builder(this@TermsActivity, R.style.CustomAlertDialog)
        val mView: View = layoutInflater.inflate(R.layout.dialog_terms, null)

        mView.findViewById<LinearLayout>(R.id.terms_dialog_btn_close)
            .setOnClickListener {
                dialogTerms.dismiss()
            }

        mBuilder.setView(mView)
        dialogTerms = mBuilder.create()
    }


    private fun showDialogTermsAndServices() {
        val displayMetrics = DisplayMetrics()

        dialogTerms.show()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        dialogTerms.window?.setLayout(displayMetrics.widthPixels - 140, LayoutParams.WRAP_CONTENT);
    }


    private fun toggleColorValidateAcceptTerms() {
        if (!statusAcceptTerms) {
            cb_acceptTermsOfServices.setButtonTintList(ColorStateList.valueOf(this@TermsActivity.getColor(R.color.danger)))
            btn_readTermsOfServices.setTextColor(ContextCompat.getColor(baseContext, R.color.danger))
        } else {
            cb_acceptTermsOfServices.setButtonTintList(ColorStateList.valueOf(this@TermsActivity.getColor(R.color.gray)))
            btn_readTermsOfServices.setTextColor(ContextCompat.getColor(baseContext, R.color.gray))
        }
    }


    private fun onContinue() {

        val numberStr = edt_phoneNumber.text.toString()
        if (numberStr.length !== 10 || numberStr[0] !== '0') {
            edt_phoneNumber.error = "Phone number invalid"
            return
        }

        if (!statusAcceptTerms) {
            this.toggleColorValidateAcceptTerms()
            return
        }

        startActivity(Intent(this@TermsActivity, MainActivity::class.java))
        finishAffinity();
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            btn_back.id ->
                finish()
            btn_readTermsOfServices.id ->
                this.showDialogTermsAndServices()
            btn_continue.id ->
                this.onContinue()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        supportActionBar?.hide()

        this.init()
    }


}