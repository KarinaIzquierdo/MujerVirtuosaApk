package com.mujer_virtuosa.data.model.user

import com.google.gson.annotations.SerializedName

data class Progress(
    val id: Int,
    val user: ProgressUser?,
    val product: ProgressProduct?,
    val description: String?,
    @SerializedName("before_image")
    val beforeImage: String?,
    @SerializedName("after_image")
    val afterImage: String?,
    @SerializedName("created_at")
    val createdAt: String?
)

data class ProgressUser(
    val id: Int,
    val name: String
)

data class ProgressProduct(
    val id: Int,
    val name: String
)

data class ProgressListData(
    @SerializedName("my_progress")
    val myProgress: List<Progress>?,
    val community: List<Progress>?
)
