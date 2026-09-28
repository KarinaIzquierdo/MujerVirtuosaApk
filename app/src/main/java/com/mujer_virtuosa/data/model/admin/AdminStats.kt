package com.mujer_virtuosa.data.model.admin

import com.google.gson.annotations.SerializedName

data class AdminStats(
    @SerializedName("total_sales")
    val totalSales: Double,
    @SerializedName("total_orders")
    val totalOrders: Int,
    @SerializedName("total_products")
    val totalProducts: Int,
    @SerializedName("total_customers")
    val totalCustomers: Int
)
