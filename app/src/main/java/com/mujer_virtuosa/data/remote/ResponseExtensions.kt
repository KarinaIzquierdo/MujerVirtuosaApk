package com.mujer_virtuosa.data.remote

import org.json.JSONObject
import retrofit2.Response

// Extrae el campo "message" del JSON de error que devuelve Laravel
fun <T> Response<T>.errorMessage(fallback: String): String {
    return try {
        val body = errorBody()?.string()
        if (body.isNullOrBlank()) {
            "$fallback: ${code()}"
        } else {
            JSONObject(body).optString("message").ifBlank {
                "$fallback: ${code()}"
            }
        }
    } catch (e: Exception) {
        "$fallback: ${code()}"
    }
}
