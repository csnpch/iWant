package com.ituy.iwant.api.authentication.dto

data class AuthenticationResponse (
    val memberId: Int,
    val username: String,
    val fullName: String,
    val tel: String
)