package com.ituy.iwant.api.member

import com.ituy.iwant.api.HttpRoutes
import com.ituy.iwant.api.deliverer.DelivererService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.PUT

interface MemberService {
    @PUT("/member")
    fun updateInfo(memberModel: MemberModel): MemberModel

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