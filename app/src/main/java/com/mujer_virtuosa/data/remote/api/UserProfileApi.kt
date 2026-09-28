package com.mujer_virtuosa.data.remote.api

import com.mujer_virtuosa.data.model.user.Address
import com.mujer_virtuosa.data.model.user.AddressRequest
import com.mujer_virtuosa.data.model.user.ProfileData
import com.mujer_virtuosa.data.model.user.ProfileUser
import com.mujer_virtuosa.data.model.user.UpdateProfileRequest
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserProfileApi {

    @GET(Constants.PROFILE_DATA_ENDPOINT)
    suspend fun getProfile(): Response<ApiResponse<ProfileData>>

    @PUT(Constants.PROFILE_DATA_ENDPOINT)
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse<ProfileUser>>

    @POST("profile/addresses")
    suspend fun createAddress(
        @Body request: AddressRequest
    ): Response<ApiResponse<Address>>

    @PUT("profile/addresses/{addressId}")
    suspend fun updateAddress(
        @Path("addressId") addressId: Int,
        @Body request: AddressRequest
    ): Response<ApiResponse<Address>>

    @DELETE("profile/addresses/{addressId}")
    suspend fun deleteAddress(
        @Path("addressId") addressId: Int
    ): Response<ApiResponse<Unit>>
}
