package com.mujer_virtuosa.data.remote.api

import com.mujer_virtuosa.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val apiService: MujerVirtuosaApi by lazy {
        retrofit.create(MujerVirtuosaApi::class.java)
    }

    val cartApiService: UserCartApi by lazy {
        retrofit.create(UserCartApi::class.java)
    }

    val progressApiService: UserProgressApi by lazy {
        retrofit.create(UserProgressApi::class.java)
    }

    val profileApiService: UserProfileApi by lazy {
        retrofit.create(UserProfileApi::class.java)
    }
}
