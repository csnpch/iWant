package com.ituy.iwant.api.wish.dto

data class CreateWishRequest (
    val title: String,
    val location: String,
    val description: String,
    val benefit: String,
    val contact: String,
)