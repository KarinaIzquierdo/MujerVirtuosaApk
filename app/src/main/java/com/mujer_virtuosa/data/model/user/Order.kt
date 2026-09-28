package com.mujer_virtuosa.data.model.user

import com.google.gson.annotations.SerializedName
import com.mujer_virtuosa.data.model.product.Product

data class Order(
    val id: Int,
    @SerializedName("order_number")
    val orderNumber: String,
    val status: String?,
    val total: Double,
    @SerializedName("estimated_date")
    val estimatedDate: String?,
    val items: List<OrderItem>?,
    @SerializedName("created_at")
    val createdAt: String?
)

data class OrderItem(
    val id: Int,
    val product: Product?,
    val quantity: Int,
    val price: Double,
    val subtotal: Double
)
