package com.mujer_virtuosa.utils

object Constants {
    // 10.0.2.2 es la dirección del host desde el emulador de Android
    // Si usas un dispositivo físico, cambia esto por la IP de tu computadora
    const val BASE_URL = "http://10.0.2.2:8001/api/"

    const val LOGIN_ENDPOINT = "auth/login"
    const val REGISTER_ENDPOINT = "auth/register"
    const val PRODUCTS_ENDPOINT = "products"
    const val CART_ENDPOINT = "cart"
    const val ORDERS_ENDPOINT = "orders"
    const val PROGRESS_ENDPOINT = "progress"
    const val PROFILE_DATA_ENDPOINT = "profile"
    const val PROFILE_ENDPOINT = "auth/profile"
    const val LOGOUT_ENDPOINT = "auth/logout"
    const val ADMIN_STATS_ENDPOINT = "admin/stats"
    const val ADMIN_PRODUCTS_ENDPOINT = "admin/products"
    const val ADMIN_SALES_ENDPOINT = "admin/sales"
    const val ADMIN_ORDERS_ENDPOINT = "admin/orders"
    const val ADMIN_USERS_ENDPOINT = "admin/users"
    const val ADMIN_PROFILE_ENDPOINT = "admin/profile"
    const val ADMIN_PROGRESS_ENDPOINT = "admin/progress"
}
