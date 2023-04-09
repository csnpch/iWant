package com.ituy.iwant.Auths

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.ituy.iwant.Stores.LocalStore
import com.ituy.iwant.api.authentication.AuthenticationService
import com.ituy.iwant.api.authentication.dto.AuthenticationRequest
import com.ituy.iwant.api.authentication.dto.AuthenticationResponse
import com.linecorp.linesdk.*
import com.linecorp.linesdk.auth.LineAuthenticationParams
import com.linecorp.linesdk.auth.LineLoginApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AuthActivity : AppCompatActivity(), View.OnClickListener {


    private val REQUEST_CODE = 1;
    private var loginType = "";
    private lateinit var auth_btnSignInLine: FlexboxLayout
    private lateinit var auth_btnSignInGoogle: FlexboxLayout
    private val LINEChannelID: String = "1660784550"
    private val apiService = AuthenticationService()

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
                if (account != null && account.idToken?.isNotEmpty() == true) {
                    val token = account.id
                    val call = token?.let { AuthenticationRequest("Google",token, account.displayName.toString(), account.email) }
                        ?.let { apiService.login(it) }
                    call?.enqueue(object: Callback<AuthenticationResponse> {
                        override fun onResponse(
                            call: Call<AuthenticationResponse>,
                            response: Response<AuthenticationResponse>
                        ) {
                            val body = response.body()
                            if (body?.status == true) {
                                val token = body?.data?.access_token
                                val tel = body?.data?.tel
                                val fullname = body?.data?.fullname
                                if (token != null) {
                                    LocalStore(this@AuthActivity).saveString("token", "Bearer $token")
                                }
                                val profile = ArrayList<String>()
                                if (fullname != null) {
                                    profile.add(fullname)
                                }
                                if (tel != null) {
                                    profile.add(tel)
                                }
                                LocalStore(this@AuthActivity).saveArrayList("profile", profile)
                                if (tel?.isNotEmpty() == true) {
                                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                                } else {
                                    startActivity(Intent(this@AuthActivity, com.ituy.iwant.Auths.TermsActivity::class.java))
                                }
                            }
                        }

                        override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
                            Toast.makeText(this@AuthActivity, t.message, Toast.LENGTH_LONG).show()
                        }
                    });
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

        when (loginType) {
            "LINE" -> {
                LineAuth.login(this, data, apiService)
            }
            "GOOGLE" -> {
                GoogleAuth.login(this, data, apiService)
            }
        }
    }

}