package com.mujer_virtuosa.data.repository.admin

import com.mujer_virtuosa.data.model.admin.AdminDevice
import com.mujer_virtuosa.data.model.admin.AdminProfile
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.data.model.admin.ChangePasswordRequest
import com.mujer_virtuosa.data.model.admin.UpdateProfileRequest
import com.mujer_virtuosa.data.remote.api.RetrofitClient
import com.mujer_virtuosa.data.remote.errorMessage

class ProfileRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getAdminProfile(): Result<ApiResponse<AdminProfile>> {
        return try {
            val response = apiService.getAdminProfile()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al obtener perfil")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(request: UpdateProfileRequest): Result<ApiResponse<AdminProfile>> {
        return try {
            val response = apiService.updateAdminProfile(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al actualizar perfil")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(request: ChangePasswordRequest): Result<ApiResponse<Unit>> {
        return try {
            val response = apiService.changeAdminPassword(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al cambiar contraseña")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDevices(): Result<ApiResponse<List<AdminDevice>>> {
        return try {
            val response = apiService.getAdminDevices()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al obtener dispositivos")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deactivateAccount(): Result<ApiResponse<Unit>> {
        return try {
            val response = apiService.deactivateAdminAccount()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al desactivar cuenta")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
