package com.ituy.iwant.api.wish

import com.ituy.iwant.api.HttpRoutes
import com.ituy.iwant.api.member.MemberService
import com.ituy.iwant.api.wish.dto.CreateWishRequest
import com.ituy.iwant.api.wish.dto.CreateWishResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface WishService {
    @Headers("Header-Version: 1")
    @GET("/wish/location/{location}")
    fun getWishByLocation(@Header("Authorization") token: String, @Path("location") location: String): Call<List<WishModel>>
    @Headers("Header-Version: 1")
    @GET("/wish/me")
    fun getWishByMe(@Header("Authorization") token: String): Call<List<WishModel>>
    @Headers("Header-Version: 1")
    @POST("/wish")
    fun createWish(@Header("Authorization") token: String, @Body wishModel: CreateWishRequest): Call<CreateWishResponse>
    @Headers("Header-Version: 1")
    @PUT("/wish/expire/{id}")
    fun updateWishExpire(@Header("Authorization") token: String, @Path("id") id: String): Call<Boolean>
    @Headers("Header-Version: 1")
    @DELETE("/wish/{id}")
    fun removeWish(@Header("Authorization") token: String, @Path("id") id: String): Call<Boolean>

    companion object {
        operator fun invoke(): WishService {
            return Retrofit.Builder()
                .baseUrl(HttpRoutes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WishService::class.java)
        }
    }

}