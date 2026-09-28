package com.mujer_virtuosa.data.repository.admin

import com.mujer_virtuosa.data.model.admin.AdminOrder
import com.mujer_virtuosa.data.model.admin.AdminSales
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.data.remote.api.RetrofitClient
import com.mujer_virtuosa.data.remote.errorMessage

class SalesRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getSales(): Result<ApiResponse<AdminSales>> {
        return try {
            val response = apiService.getAdminSales()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al obtener ventas")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOrders(): Result<ApiResponse<List<AdminOrder>>> {
        return try {
            val response = apiService.getAdminOrders()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al obtener pedidos")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
