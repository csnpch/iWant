package com.ituy.iwant.api.wish

import com.ituy.iwant.api.HttpRoutes
import com.ituy.iwant.api.member.MemberService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface WishService {
    @GET("/wish/{location}")
    fun getWishByLocation(location: String): List<WishModel>
    @POST("/wish")
    fun createWish(wishModel: WishModel)
    @PUT("/wish")
    fun updateWish(wishModel: WishModel)
    @DELETE("/wish/{id}")
    fun removeWish(id: Int): Boolean

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