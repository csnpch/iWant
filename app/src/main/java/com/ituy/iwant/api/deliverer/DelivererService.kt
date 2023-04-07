package com.ituy.iwant.api.deliverer

import com.ituy.iwant.api.HttpRoutes
import com.ituy.iwant.api.authentication.AuthenticationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DelivererService {

    @Headers("Header-Version: 1")
    @GET("/deliverer/{id}")
    fun getDelivererByWishID(@Path("id") id: Int): List<DelivererModel>
    @Headers("Header-Version: 1")
    @POST("/deliverer")
    fun createDeliverer(@Body wishID: Int)
    @Headers("Header-Version: 1")
    @DELETE("/deliverer/{id}")
    fun removeDeliverer(@Path("id") wishID: Int): Boolean

    companion object {
        fun retrofitBuild(): DelivererService {
            return Retrofit.Builder()
                .baseUrl(HttpRoutes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DelivererService::class.java)
        }
    }

}