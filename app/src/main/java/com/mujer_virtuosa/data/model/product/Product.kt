package com.mujer_virtuosa.data.model.product

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val benefits: String?,
    val ingredients: String?,
    val price: Double,
    val stock: Int,
    val image: String?,
    val category: String?,
    val active: Boolean,
    @SerializedName("created_at")
    val createdAt: String?
)
