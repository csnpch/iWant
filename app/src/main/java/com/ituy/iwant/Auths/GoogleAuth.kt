package com.ituy.iwant.Auths

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.ituy.iwant.api.authentication.AuthenticationService
import com.ituy.iwant.api.authentication.dto.AuthenticationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class GoogleAuth {
    companion object {
        fun login(data: Intent?): String {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            return try {
                val account = task.result
                println(account.id)
                println(account.idToken)
                "SUCCESS"
            } catch (e: Exception) {
                "Google Login Error"
            }
        }
    }
}
