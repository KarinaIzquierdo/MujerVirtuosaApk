package com.mujer_virtuosa.data.repository.user

import com.mujer_virtuosa.data.model.user.Address
import com.mujer_virtuosa.data.model.user.AddressRequest
import com.mujer_virtuosa.data.model.user.ProfileData
import com.mujer_virtuosa.data.model.user.ProfileUser
import com.mujer_virtuosa.data.model.user.UpdateProfileRequest
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.data.remote.api.RetrofitClient
import com.mujer_virtuosa.data.remote.errorMessage

class ProfileRepository {

    private val apiService = RetrofitClient.profileApiService

    suspend fun getProfile(): Result<ApiResponse<ProfileData>> {
        return try {
            val response = apiService.getProfile()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al obtener el perfil")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(
        name: String,
        email: String,
        phone: String?
    ): Result<ApiResponse<ProfileUser>> {
        return try {
            val response = apiService.updateProfile(
                UpdateProfileRequest(name, email, phone)
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al actualizar el perfil")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createAddress(
        address: String,
        isDefault: Boolean = false
    ): Result<ApiResponse<Address>> {
        return try {
            val response = apiService.createAddress(AddressRequest(address, isDefault))
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al agregar la dirección")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAddress(
        addressId: Int,
        address: String,
        isDefault: Boolean = false
    ): Result<ApiResponse<Address>> {
        return try {
            val response = apiService.updateAddress(
                addressId,
                AddressRequest(address, isDefault)
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al actualizar la dirección")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAddress(addressId: Int): Result<ApiResponse<Unit>> {
        return try {
            val response = apiService.deleteAddress(addressId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al eliminar la dirección")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
