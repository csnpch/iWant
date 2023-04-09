package com.ituy.iwant.api.deliverer

import com.ituy.iwant.api.member.MemberModel
import java.util.*

data class DelivererModel (
    val id: Int,
    val wishId: Int,
    val memberId: Int,
    val createdAt: Date,
    val member: MemberModel
)