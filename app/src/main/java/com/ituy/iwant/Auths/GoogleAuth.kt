package com.ituy.iwant.Auths

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.ituy.iwant.MainActivity
import com.ituy.iwant.Stores.LocalStore
import com.ituy.iwant.api.authentication.AuthenticationService
import com.ituy.iwant.api.authentication.dto.AuthenticationRequest
import com.ituy.iwant.api.authentication.dto.AuthenticationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import kotlin.math.log

class GoogleAuth {
    companion object {
        fun login(context: Context, data: Intent?, api: AuthenticationService) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.result
                if (account.id == null) {
                    Toast.makeText(context, "Not Found ID", Toast.LENGTH_LONG).show()
                }
                val data = AuthenticationRequest("Google",account.id!!, account.displayName.toString(), account.email)
                val call = api.login(data)
                call.enqueue(object: Callback<AuthenticationResponse> {
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
                                    LocalStore(context).saveString("token", "Bearer $token")
                                }
                                val profile = ArrayList<String>()
                                if (fullname != null) {
                                    profile.add(fullname)
                                }
                                if (tel != null) {
                                    profile.add(tel)
                                }
                                LocalStore(context).saveArrayList("profile", profile)
                                if (token?.isNotEmpty() == true && tel != null) {
                                    ContextCompat.startActivity(
                                        context,
                                        Intent(context, MainActivity::class.java),
                                        null
                                    )
                                } else {
                                    ContextCompat.startActivity(
                                        context,
                                        Intent(context, TermsActivity::class.java),
                                        null
                                    )
                                }
                            }
                        }

                        override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    }

                })
            } catch (e: Exception) {
                Toast.makeText(context, "Google Login Error", Toast.LENGTH_LONG).show()
            }
        }
    }
}
