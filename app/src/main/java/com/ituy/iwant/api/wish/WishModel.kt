package com.ituy.iwant.api.wish

data class WishModel (
    val wishID: Int,
    val ownerWishName: String ,
    val title: String,
    val location: String,
    val description: String,
    val benefit: String,
    val contact: String,
    val delivererWishName: String,
    val accept: Boolean
)