package com.mujer_virtuosa.data.remote.api

import com.mujer_virtuosa.data.model.user.AddToCartRequest
import com.mujer_virtuosa.data.model.user.Cart
import com.mujer_virtuosa.data.model.user.Order
import com.mujer_virtuosa.data.model.user.UpdateCartItemRequest
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserCartApi {

    @GET(Constants.CART_ENDPOINT)
    suspend fun getCart(): Response<ApiResponse<Cart>>

    @POST(Constants.CART_ENDPOINT)
    suspend fun addToCart(@Body request: AddToCartRequest): Response<ApiResponse<Cart>>

    @PUT("cart/{itemId}")
    suspend fun updateCartItem(
        @Path("itemId") itemId: Int,
        @Body request: UpdateCartItemRequest
    ): Response<ApiResponse<Cart>>

    @DELETE("cart/{itemId}")
    suspend fun removeCartItem(@Path("itemId") itemId: Int): Response<ApiResponse<Unit>>

    @GET(Constants.ORDERS_ENDPOINT)
    suspend fun getOrders(): Response<ApiResponse<List<Order>>>

    @POST(Constants.ORDERS_ENDPOINT)
    suspend fun checkout(): Response<ApiResponse<Order>>
}
