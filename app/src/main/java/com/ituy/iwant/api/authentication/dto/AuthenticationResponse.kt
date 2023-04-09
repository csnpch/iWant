package com.ituy.iwant.api.authentication.dto

data class AuthenticationResponse (
    val status: Boolean,
    val message: String,
    val data: AuthenticationResponseData,
)

data class AuthenticationResponseData (
    val access_token: String,
    val fullname: String,
    val tel: String,
)
