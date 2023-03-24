package com.ituy.iwant.Auths

import android.content.Intent
import com.linecorp.linesdk.LineApiResponseCode
import com.linecorp.linesdk.auth.LineLoginApi
import com.linecorp.linesdk.auth.LineLoginResult

class LineAuth {
    companion object {

        private val LINEChannelID: String = "1660784550"

        fun login(data: Intent?): String {
            val result: LineLoginResult = LineLoginApi.getLoginResultFromIntent(data)
            when (result.responseCode) {
                LineApiResponseCode.SUCCESS -> {
                    val accessToken: String = result.lineCredential?.accessToken?.tokenString ?: ""
                    if (accessToken.isEmpty()) {
                        return "Line Not Found Token"
                    }
                    val userID: String = result.lineProfile?.userId.toString()
                    return "SUCCESS"
                }
                LineApiResponseCode.CANCEL -> {
                    return "LINE Login Canceled by user."
                }
                else -> {
                    return "LINE Login FAILED!"
                }
            }
            return "LINE Login FAILED!"
        }
    }

}