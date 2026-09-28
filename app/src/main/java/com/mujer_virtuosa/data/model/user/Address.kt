package com.mujer_virtuosa.data.model.user

import com.google.gson.annotations.SerializedName

data class Address(
    val id: Int,
    val address: String,
    @SerializedName("is_default")
    val isDefault: Boolean
)

data class ProfileData(
    val user: ProfileUser?,
    val addresses: List<Address>?
)

data class ProfileUser(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    val role: String?
)

data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val phone: String?
)

data class AddressRequest(
    val address: String,
    @SerializedName("is_default")
    val isDefault: Boolean = false
)
