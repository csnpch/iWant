package com.ituy.iwant.api.wish

import com.ituy.iwant.api.member.MemberModel
import java.util.*

data class WishModel (
    val id: Int,
    val member_id: String,
    val title: String,
    val location: String,
    val description: String,
    val benefit: String,
    val contact: String,
    val expire: Date,
    val createdAt: Date,
    val deliverers: List<MemberModel>
)