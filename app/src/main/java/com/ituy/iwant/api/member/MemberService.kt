package com.ituy.iwant.api.member

import com.ituy.iwant.api.HttpRoutes
import com.ituy.iwant.api.deliverer.DelivererService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT

interface MemberService {
    @Headers("Header-Version: 1")
    @PUT("/member")
    fun updateInfo(@Body memberModel: MemberModel): MemberModel

    companion object {
        fun retrofitBuild(): MemberService {
            return Retrofit.Builder()
                .baseUrl(HttpRoutes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MemberService::class.java)
        }
    }

}