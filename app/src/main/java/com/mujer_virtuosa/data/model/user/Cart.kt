package com.mujer_virtuosa.data.model.user

import com.google.gson.annotations.SerializedName
import com.mujer_virtuosa.data.model.product.Product

data class Cart(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    val status: String?,
    val total: Double,
    val items: List<CartItem>?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)

data class CartItem(
    val id: Int,
    val product: Product?,
    val quantity: Int,
    val price: Double,
    val selected: Boolean,
    val subtotal: Double
)

data class AddToCartRequest(
    @SerializedName("product_id")
    val productId: Int,
    val quantity: Int
)

data class UpdateCartItemRequest(
    val quantity: Int,
    val selected: Boolean? = null
)
