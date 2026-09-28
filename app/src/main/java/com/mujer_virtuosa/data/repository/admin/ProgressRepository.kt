package com.mujer_virtuosa.data.repository.admin

import com.mujer_virtuosa.data.model.admin.AdminProgressResponse
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.data.remote.api.RetrofitClient
import com.mujer_virtuosa.data.remote.errorMessage

class ProgressRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getProgress(search: String? = null, page: Int = 1): Result<ApiResponse<AdminProgressResponse>> {
        return try {
            val response = apiService.getAdminProgress(
                search = search?.takeIf { it.isNotBlank() },
                page = page
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al obtener progreso")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
