package com.mujer_virtuosa.data.model.auth

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String?,
    @SerializedName("created_at")
    val createdAt: String?
)
