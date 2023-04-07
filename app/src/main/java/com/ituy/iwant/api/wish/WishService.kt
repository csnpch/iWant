package com.ituy.iwant.api.wish

import com.ituy.iwant.api.HttpRoutes
import com.ituy.iwant.api.member.MemberService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface WishService {
    @Headers("Header-Version: 1")
    @GET("/wish/{location}")
    fun getWishByLocation(@Path("location") location: String): List<WishModel>
    @Headers("Header-Version: 1")
    @POST("/wish")
    fun createWish(@Body wishModel: WishModel)
    @Headers("Header-Version: 1")
    @PUT("/wish")
    fun updateWish(@Body wishModel: WishModel)
    @Headers("Header-Version: 1")
    @DELETE("/wish/{id}")
    fun removeWish(@Path("id") id: Int): Boolean

    companion object {
        fun retrofitBuild(): WishService {
            return Retrofit.Builder()
                .baseUrl(HttpRoutes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WishService::class.java)
        }
    }

}