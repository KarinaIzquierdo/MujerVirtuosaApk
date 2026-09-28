package com.mujer_virtuosa.data.model.admin

import com.google.gson.annotations.SerializedName

data class AdminProgressResponse(
    @SerializedName("users_with_progress") val usersWithProgress: Int,
    @SerializedName("total_progresses") val totalProgresses: Int,
    @SerializedName("progresses") val progresses: List<AdminProgress>,
    @SerializedName("pagination") val pagination: PaginationMeta
)

data class AdminProgress(
    @SerializedName("id") val id: Int,
    @SerializedName("user") val user: ProgressUser,
    @SerializedName("product") val product: ProgressProduct,
    @SerializedName("before_image") val beforeImage: String?,
    @SerializedName("after_image") val afterImage: String?,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("testimony") val testimony: String?
)

data class ProgressUser(
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String?
)

data class ProgressProduct(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String?
)
