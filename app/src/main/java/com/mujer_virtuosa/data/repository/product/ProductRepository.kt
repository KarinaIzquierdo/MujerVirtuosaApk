package com.mujer_virtuosa.data.repository.product

import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.data.model.product.Product
import com.mujer_virtuosa.data.remote.api.RetrofitClient
import com.mujer_virtuosa.data.remote.errorMessage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProductRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getProducts(): Result<ApiResponse<List<Product>>> {
        return try {
            val response = apiService.getProducts()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al obtener productos")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProduct(
        name: String,
        price: String,
        stock: String,
        description: String,
        category: String,
        imageFile: File?
    ): Result<ApiResponse<Product>> {
        return try {
            val textType = "text/plain".toMediaTypeOrNull()
            val imagePart = imageFile?.let {
                val body = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", it.name, body)
            }

            val response = apiService.createProduct(
                name = name.toRequestBody(textType),
                price = price.toRequestBody(textType),
                stock = stock.toRequestBody(textType),
                description = description.takeIf { it.isNotBlank() }?.toRequestBody(textType),
                category = category.takeIf { it.isNotBlank() }?.toRequestBody(textType),
                image = imagePart
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al crear producto")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(
        id: Int,
        name: String,
        price: String,
        stock: String,
        description: String,
        category: String,
        active: Boolean,
        imageFile: File?
    ): Result<ApiResponse<Product>> {
        return try {
            val textType = "text/plain".toMediaTypeOrNull()
            val imagePart = imageFile?.let {
                val body = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", it.name, body)
            }

            val response = apiService.updateProduct(
                id = id,
                method = "PUT".toRequestBody(textType),
                name = name.toRequestBody(textType),
                price = price.toRequestBody(textType),
                stock = stock.toRequestBody(textType),
                description = description.takeIf { it.isNotBlank() }?.toRequestBody(textType),
                category = category.takeIf { it.isNotBlank() }?.toRequestBody(textType),
                active = (if (active) "1" else "0").toRequestBody(textType),
                image = imagePart
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al actualizar producto")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
