package com.ituy.iwant.api.deliverer

import com.ituy.iwant.api.HttpRoutes
import com.ituy.iwant.api.authentication.AuthenticationService
import com.ituy.iwant.api.deliverer.dto.DelivererResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DelivererService {

    @Headers("Header-Version: 1")
    @POST("/deliverer/{id}")
    fun createDeliverer(@Header("Authorization") token: String, @Path("id") id: String): Call<DelivererResponse>

    companion object {
        operator fun invoke(): DelivererService {
            return Retrofit.Builder()
                .baseUrl(HttpRoutes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DelivererService::class.java)
        }
    }

}