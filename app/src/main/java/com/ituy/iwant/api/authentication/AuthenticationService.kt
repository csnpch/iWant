package com.ituy.iwant.api.authentication

import com.ituy.iwant.api.HttpRoutes
import com.ituy.iwant.api.authentication.dto.AuthenticationRequest
import com.ituy.iwant.api.authentication.dto.AuthenticationResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthenticationService {

   @Headers("Header-Version: 1")
   @POST("/authentication/")
   fun login(@Body data: AuthenticationRequest): Call<AuthenticationResponse>

   companion object {
      operator fun invoke():AuthenticationService {
         return Retrofit.Builder()
            .baseUrl(HttpRoutes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthenticationService::class.java)
      }
   }

}