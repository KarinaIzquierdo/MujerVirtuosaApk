package com.mujer_virtuosa.data.repository.admin

import com.mujer_virtuosa.data.model.admin.AdminStats
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.data.remote.api.RetrofitClient
import com.mujer_virtuosa.data.remote.errorMessage

class DashboardRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getStats(): Result<ApiResponse<AdminStats>> {
        return try {
            val response = apiService.getAdminStats()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al obtener estadísticas")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
