package com.mujer_virtuosa.data.repository.admin

import com.mujer_virtuosa.data.model.admin.AdminUsers
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.data.remote.api.RetrofitClient
import com.mujer_virtuosa.data.remote.errorMessage

class UsersRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getUsers(search: String? = null, page: Int = 1): Result<ApiResponse<AdminUsers>> {
        return try {
            val response = apiService.getAdminUsers(
                search = search?.takeIf { it.isNotBlank() },
                page = page
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al obtener usuarios")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
