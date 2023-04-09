package com.ituy.iwant.api.member

import java.util.*

data class MemberModel (
    val memberID: Int,
    val fullName: String,
    val tel: String,
    val email: String,
    val authType: String,
    val createdAt: Date,
)