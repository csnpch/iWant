package com.ituy.iwant.Auths

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.ituy.iwant.MainActivity
import com.ituy.iwant.Stores.LocalStore
import com.ituy.iwant.api.authentication.AuthenticationService
import com.ituy.iwant.api.authentication.dto.AuthenticationRequest
import com.ituy.iwant.api.authentication.dto.AuthenticationResponse
import com.linecorp.linesdk.LineApiResponseCode
import com.linecorp.linesdk.auth.LineLoginApi
import com.linecorp.linesdk.auth.LineLoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class LineAuth {
    companion object {

        private val LINEChannelID: String = "1660784550"

        fun login(context: Context, data: Intent?, api: AuthenticationService) {
            val result: LineLoginResult = LineLoginApi.getLoginResultFromIntent(data)
            when (result.responseCode) {
                LineApiResponseCode.SUCCESS -> {
                    val accessToken: String = result.lineCredential?.accessToken?.tokenString ?: ""
                    if (accessToken.isEmpty()) {
                        Toast.makeText(context, "Line Not Found Token", Toast.LENGTH_LONG).show()
                    }
                    val userID: String = result.lineProfile?.userId.toString()
                    val name: String = result.lineProfile?.displayName.toString()
                    val data = AuthenticationRequest("Line",userID, name, "")
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
                                Log.e("toklen", token.toString())
                                LocalStore(context).saveArrayList("profile", profile)
                                if (token?.isNotEmpty() == true && tel != null) {
                                    startActivity(context, Intent(context, MainActivity::class.java), null)
                                } else {
                                    startActivity(context, Intent(context, TermsActivity::class.java), null)
                                }
                            }
                        }

                        override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
                            Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                        }

                    })
                }
                LineApiResponseCode.CANCEL -> {
                    Toast.makeText(context, "LINE Login Canceled by user.", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(context, "LINE Login FAILED!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}