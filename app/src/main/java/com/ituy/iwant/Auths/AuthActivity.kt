package com.ituy.iwant.Auths

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ituy.iwant.R
import com.google.android.flexbox.FlexboxLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.ituy.iwant.MainActivity
import com.linecorp.linesdk.*
import com.linecorp.linesdk.auth.LineAuthenticationParams
import com.linecorp.linesdk.auth.LineLoginApi
import java.util.*


class AuthActivity : AppCompatActivity(), View.OnClickListener {


    private val REQUEST_CODE = 1;
    private var loginType = "";
    private lateinit var auth_btnSignInLine: FlexboxLayout
    private lateinit var auth_btnSignInGoogle: FlexboxLayout
    private val LINEChannelID: String = "1660784550"


    private fun init() {
        auth_btnSignInLine = findViewById(R.id.auth_btnSignInLine)
        auth_btnSignInLine.setOnClickListener(this)

        auth_btnSignInGoogle = findViewById(R.id.auth_btnSignInGoogle)
        auth_btnSignInGoogle.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            auth_btnSignInLine.id -> {
                loginType = "LINE"
                try {
                    val loginIntent: Intent = LineLoginApi
                        .getLoginIntent(v.context, LINEChannelID, LineAuthenticationParams.Builder()
                            .scopes(Arrays.asList(Scope.PROFILE)).build())
                    startActivityForResult(loginIntent, REQUEST_CODE)
                } catch(e: Exception) {
                    Toast.makeText(this, "Line Login Error", Toast.LENGTH_LONG).show()
                }
            }
            auth_btnSignInGoogle.id -> {
                loginType = "GOOGLE"
                val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("680036386293-58s0621qpmvv4boktdssjn0630lmued5.apps.googleusercontent.com")
                    .requestEmail().build()
                val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
                val account: GoogleSignInAccount? = GoogleSignIn
                    .getLastSignedInAccount(this)
                if (account != null) {
                    startActivity(Intent(this@AuthActivity, com.ituy.iwant.Auths.TermsActivity::class.java))
                }
                startActivityForResult(googleSignInClient.signInIntent, REQUEST_CODE)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportActionBar?.hide()

        this.init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode != REQUEST_CODE) {
            Toast.makeText(this, "Can't Connected To Server Line", Toast.LENGTH_LONG).show()
            return
        }

        var errorMessage: String = ""

        if (loginType.equals("LINE")) {
            errorMessage = LineAuth.login(data)
        } else if (loginType.equals("GOOGLE")) {
            errorMessage = GoogleAuth.login(data)
        } else {
            return
        }

        if (!errorMessage.equals("SUCCESS")) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            return
        }

        if (false) {
            startActivity(Intent(this@AuthActivity, MainActivity::class.java))
        }

    }

}