package com.mujer_virtuosa.data.remote.api

import com.mujer_virtuosa.data.model.user.Progress
import com.mujer_virtuosa.data.model.user.ProgressListData
import com.mujer_virtuosa.data.remote.ApiResponse
import com.mujer_virtuosa.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserProgressApi {

    @GET(Constants.PROGRESS_ENDPOINT)
    suspend fun getProgress(): Response<ApiResponse<ProgressListData>>

    @Multipart
    @POST(Constants.PROGRESS_ENDPOINT)
    suspend fun createProgress(
        @Part("product_id") productId: RequestBody,
        @Part("description") description: RequestBody,
        @Part beforeImage: MultipartBody.Part,
        @Part afterImage: MultipartBody.Part
    ): Response<ApiResponse<Progress>>
}
