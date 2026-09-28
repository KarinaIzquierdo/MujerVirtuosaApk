package com.mujer_virtuosa.data.repository.user

import com.mujer_virtuosa.data.model.user.Order
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.data.remote.api.RetrofitClient
import com.mujer_virtuosa.data.remote.errorMessage

class OrderRepository {

    private val apiService = RetrofitClient.cartApiService

    suspend fun getOrders(): Result<ApiResponse<List<Order>>> {
        return try {
            val response = apiService.getOrders()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al obtener los pedidos")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
