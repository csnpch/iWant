package com.ituy.iwant.api.deliverer

import com.ituy.iwant.api.HttpRoutes
import com.ituy.iwant.api.authentication.AuthenticationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface DelivererService {

    @GET("/deliverer/{id}")
    fun getDelivererByWishID(id: Int): List<DelivererModel>
    @POST("/deliverer")
    fun createDeliverer(wishID: Int)
    @DELETE("/deliverer/{id}")
    fun removeDeliverer(wishID: Int): Boolean

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