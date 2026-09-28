package com.mujer_virtuosa.data.model.admin

import com.google.gson.annotations.SerializedName

data class AdminDevice(
    @SerializedName("id") val id: String,
    @SerializedName("ip_address") val ipAddress: String?,
    @SerializedName("device") val device: String,
    @SerializedName("last_activity") val lastActivity: String,
    @SerializedName("is_current") val isCurrent: Boolean
)
