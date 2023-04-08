package com.ituy.iwant.api.wish

data class WishModel (
    val id: Int,
    val member_id: String ,
    val title: String,
    val location: String,
    val description: String,
    val benefit: String,
    val contact: String,
    val accept: Boolean
)