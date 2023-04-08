package com.ituy.iwant.api.authentication.dto

data class AuthenticationRequest (
    val authType: String,
    val userID: String,
    val fullname: String,
    val email: String?,
)