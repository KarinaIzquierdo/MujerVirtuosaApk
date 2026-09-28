package com.mujer_virtuosa.data.model.admin

import com.google.gson.annotations.SerializedName

data class AdminProfile(
    @SerializedName("id") val id: Int,
    @SerializedName("user_code") val userCode: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("role") val role: String,
    @SerializedName("role_label") val roleLabel: String,
    @SerializedName("member_since") val memberSince: String,
    @SerializedName("last_login") val lastLogin: String?,
    @SerializedName("password_updated_at") val passwordUpdatedAt: String?,
    @SerializedName("active_devices") val activeDevices: Int
)

data class UpdateProfileRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?
)

data class ChangePasswordRequest(
    @SerializedName("current_password") val currentPassword: String,
    @SerializedName("new_password") val newPassword: String,
    @SerializedName("new_password_confirmation") val newPasswordConfirmation: String
)
