package com.mujer_virtuosa.data.model.admin

import com.google.gson.annotations.SerializedName

data class AdminUsers(
    @SerializedName("total_users") val totalUsers: Int,
    @SerializedName("active_users") val activeUsers: Int,
    @SerializedName("new_users_month") val newUsersMonth: Int,
    @SerializedName("users") val users: List<AdminUser>,
    @SerializedName("pagination") val pagination: PaginationMeta
)

data class AdminUser(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("registered_at") val registeredAt: String,
    @SerializedName("last_login") val lastLogin: String?,
    @SerializedName("orders_count") val ordersCount: Int
)

data class PaginationMeta(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("last_page") val lastPage: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("from") val from: Int,
    @SerializedName("to") val to: Int
)
