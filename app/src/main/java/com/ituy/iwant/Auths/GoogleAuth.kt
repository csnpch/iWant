package com.ituy.iwant.Auths

import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

class GoogleAuth {
    companion object {
        fun login(data: Intent?): String {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            return try {
                System.out.println(task.getResult().email)
                "SUCCESS"
            } catch (e: Exception) {
                System.out.println(e)
                "Google Login Error"
            }
        }
    }
}