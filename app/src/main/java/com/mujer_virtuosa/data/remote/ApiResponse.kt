package com.mujer_virtuosa.data.remote

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    @SerializedName("data")
    val data: T? = null,
    val token: String? = null
)
