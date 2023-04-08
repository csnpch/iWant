package com.ituy.iwant.api.member

import com.ituy.iwant.api.HttpRoutes
import com.ituy.iwant.api.deliverer.DelivererService
import com.ituy.iwant.api.member.dto.MemberRequest
import com.ituy.iwant.api.member.dto.MemberResponse
import com.ituy.iwant.api.member.dto.MemberTelRequest
import com.ituy.iwant.api.member.dto.MemberTelResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MemberService {
    @Headers("Header-Version: 1")
    @GET("member/")
    fun profile(@Header("Authorization") token: String): Call<MemberModel>

    @Headers("Header-Version: 1")
    @PUT("member/tel/")
    fun updateTel(@Header("Authorization") token: String, @Body memberTelModel: MemberTelRequest): Call<MemberTelResponse>

    @Headers("Header-Version: 1")
    @PATCH("member/")
    fun update(@Header("Authorization") token: String, @Body memberRequest: MemberRequest): Call<MemberResponse>

    companion object {
        operator fun invoke(): MemberService {
            return Retrofit.Builder()
                .baseUrl(HttpRoutes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MemberService::class.java)
        }
    }

}