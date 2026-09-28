package com.mujer_virtuosa.data.repository.user

import com.mujer_virtuosa.data.model.user.AddToCartRequest
import com.mujer_virtuosa.data.model.user.Cart
import com.mujer_virtuosa.data.model.user.Order
import com.mujer_virtuosa.data.model.user.UpdateCartItemRequest
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.data.remote.api.RetrofitClient
import com.mujer_virtuosa.data.remote.errorMessage

class CartRepository {

    private val apiService = RetrofitClient.cartApiService

    suspend fun getCart(): Result<ApiResponse<Cart>> {
        return try {
            val response = apiService.getCart()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al obtener el carrito")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToCart(productId: Int, quantity: Int): Result<ApiResponse<Cart>> {
        return try {
            val response = apiService.addToCart(AddToCartRequest(productId, quantity))
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al agregar al carrito")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateItem(itemId: Int, quantity: Int, selected: Boolean? = null): Result<ApiResponse<Cart>> {
        return try {
            val response = apiService.updateCartItem(itemId, UpdateCartItemRequest(quantity, selected))
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al actualizar el carrito")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeItem(itemId: Int): Result<ApiResponse<Unit>> {
        return try {
            val response = apiService.removeCartItem(itemId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al eliminar el producto")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkout(): Result<ApiResponse<Order>> {
        return try {
            val response = apiService.checkout()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al procesar la compra")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
