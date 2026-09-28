package com.mujer_virtuosa.utils

import android.content.Context
import android.content.SharedPreferences

object TokenManager {

    private const val PREFS_NAME = "mujer_virtuosa_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_REMEMBERED_EMAIL = "remembered_email"
    private const val KEY_USER_ROLE = "user_role"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    fun hasToken(): Boolean {
        return !getToken().isNullOrBlank()
    }

    fun saveRememberedEmail(email: String) {
        prefs.edit().putString(KEY_REMEMBERED_EMAIL, email).apply()
    }

    fun getRememberedEmail(): String? {
        return prefs.getString(KEY_REMEMBERED_EMAIL, null)
    }

    fun clearRememberedEmail() {
        prefs.edit().remove(KEY_REMEMBERED_EMAIL).apply()
    }

    fun saveUserRole(role: String) {
        prefs.edit().putString(KEY_USER_ROLE, role).apply()
    }

    fun getUserRole(): String? {
        return prefs.getString(KEY_USER_ROLE, null)
    }

    fun clearSession() {
        prefs.edit()
            .remove(KEY_TOKEN)
            .remove(KEY_USER_ROLE)
            .apply()
    }
}
