package com.mujer_virtuosa.data.remote.api

import com.mujer_virtuosa.data.model.admin.AdminDevice
import com.mujer_virtuosa.data.model.admin.AdminOrder
import com.mujer_virtuosa.data.model.admin.AdminProfile
import com.mujer_virtuosa.data.model.admin.AdminProgressResponse
import com.mujer_virtuosa.data.model.admin.AdminSales
import com.mujer_virtuosa.data.model.admin.AdminStats
import com.mujer_virtuosa.data.model.admin.AdminUsers
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.data.model.admin.ChangePasswordRequest
import com.mujer_virtuosa.data.model.auth.LoginRequest
import com.mujer_virtuosa.data.model.product.Product
import com.mujer_virtuosa.data.model.auth.RegisterRequest
import com.mujer_virtuosa.data.model.admin.UpdateProfileRequest
import com.mujer_virtuosa.data.model.auth.User
import com.mujer_virtuosa.data.model.user.AddToCartRequest
import com.mujer_virtuosa.data.model.user.Cart
import com.mujer_virtuosa.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MujerVirtuosaApi {

    @POST(Constants.LOGIN_ENDPOINT)
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<User>>

    @POST(Constants.REGISTER_ENDPOINT)
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<User>>

    @GET(Constants.PRODUCTS_ENDPOINT)
    suspend fun getProducts(): Response<ApiResponse<List<Product>>>

    @GET(Constants.CART_ENDPOINT)
    suspend fun getCart(): Response<ApiResponse<Cart>>

    @POST(Constants.CART_ENDPOINT)
    suspend fun addToCart(@Body request: AddToCartRequest): Response<ApiResponse<Cart>>

    @GET(Constants.PROFILE_ENDPOINT)
    suspend fun getProfile(): Response<ApiResponse<User>>

    @POST(Constants.LOGOUT_ENDPOINT)
    suspend fun logout(): Response<ApiResponse<Unit>>

    @GET(Constants.ADMIN_STATS_ENDPOINT)
    suspend fun getAdminStats(): Response<ApiResponse<AdminStats>>

    @GET(Constants.ADMIN_SALES_ENDPOINT)
    suspend fun getAdminSales(): Response<ApiResponse<AdminSales>>

    @GET(Constants.ADMIN_ORDERS_ENDPOINT)
    suspend fun getAdminOrders(): Response<ApiResponse<List<AdminOrder>>>

    @GET(Constants.ADMIN_USERS_ENDPOINT)
    suspend fun getAdminUsers(
        @Query("search") search: String?,
        @Query("page") page: Int
    ): Response<ApiResponse<AdminUsers>>

    @GET(Constants.ADMIN_PROGRESS_ENDPOINT)
    suspend fun getAdminProgress(
        @Query("search") search: String?,
        @Query("page") page: Int
    ): Response<ApiResponse<AdminProgressResponse>>

    @GET(Constants.ADMIN_PROFILE_ENDPOINT)
    suspend fun getAdminProfile(): Response<ApiResponse<AdminProfile>>

    @PUT(Constants.ADMIN_PROFILE_ENDPOINT)
    suspend fun updateAdminProfile(
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse<AdminProfile>>

    @PUT("admin/profile/password")
    suspend fun changeAdminPassword(
        @Body request: ChangePasswordRequest
    ): Response<ApiResponse<Unit>>

    @GET("admin/profile/devices")
    suspend fun getAdminDevices(): Response<ApiResponse<List<AdminDevice>>>

    @POST("admin/profile/deactivate")
    suspend fun deactivateAdminAccount(): Response<ApiResponse<Unit>>

    @Multipart
    @POST(Constants.ADMIN_PRODUCTS_ENDPOINT)
    suspend fun createProduct(
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("description") description: RequestBody?,
        @Part("category") category: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<ApiResponse<Product>>

    // POST + _method=PUT porque PHP no parsea multipart en peticiones PUT
    @Multipart
    @POST("admin/products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Part("_method") method: RequestBody,
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("description") description: RequestBody?,
        @Part("category") category: RequestBody?,
        @Part("active") active: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ApiResponse<Product>>
}
