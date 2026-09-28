package com.mujer_virtuosa.data.repository.user

import android.content.Context
import android.net.Uri
import com.mujer_virtuosa.data.model.user.Progress
import com.mujer_virtuosa.data.model.user.ProgressListData
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.data.remote.api.RetrofitClient
import com.mujer_virtuosa.data.remote.errorMessage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProgressRepository {

    private val apiService = RetrofitClient.progressApiService

    suspend fun getProgress(): Result<ApiResponse<ProgressListData>> {
        return try {
            val response = apiService.getProgress()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al obtener el progreso")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProgress(
        context: Context,
        productId: Int,
        description: String,
        beforeImageUri: Uri,
        afterImageUri: Uri
    ): Result<ApiResponse<Progress>> {
        return try {
            val productIdBody = productId.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody = description
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val beforePart = uriToPart(context, beforeImageUri, "before_image")
                ?: return Result.failure(Exception("No se pudo leer la foto de inicio"))
            val afterPart = uriToPart(context, afterImageUri, "after_image")
                ?: return Result.failure(Exception("No se pudo leer la foto de progreso"))

            val response = apiService.createProgress(
                productIdBody, descriptionBody, beforePart, afterPart
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorMessage("Error al guardar el progreso")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun uriToPart(
        context: Context,
        uri: Uri,
        fieldName: String
    ): MultipartBody.Part? {
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: return null
        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
        val extension = when {
            mimeType.contains("png") -> "png"
            mimeType.contains("webp") -> "webp"
            else -> "jpg"
        }
        val body = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fieldName, "progress.$extension", body)
    }
}
