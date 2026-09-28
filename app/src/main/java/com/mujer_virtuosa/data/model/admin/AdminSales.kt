package com.mujer_virtuosa.data.model.admin

import com.google.gson.annotations.SerializedName

data class AdminSales(
    @SerializedName("total_sales") val totalSales: Double,
    @SerializedName("total_orders") val totalOrders: Int,
    @SerializedName("recent_orders") val recentOrders: List<AdminOrder>,
    @SerializedName("chart_data") val chartData: List<ChartPoint>
)

data class AdminOrder(
    @SerializedName("id") val id: Int,
    @SerializedName("order_number") val orderNumber: String,
    @SerializedName("date") val date: String,
    @SerializedName("customer") val customer: String,
    @SerializedName("products_count") val productsCount: Int,
    @SerializedName("total") val total: Double,
    @SerializedName("status") val status: String
)

data class ChartPoint(
    @SerializedName("date") val date: String,
    @SerializedName("sales") val sales: Double,
    @SerializedName("orders") val orders: Int
)
