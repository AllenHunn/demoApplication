package com.commentsolddemo.awesomestore.handlers

import com.commentsolddemo.awesomestore.AuthenticationStore
import com.commentsolddemo.awesomestore.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseHandler(includeJWT: Boolean = false) {
    private val httpClient: OkHttpClient by lazy {
        val httpLogger = HttpLoggingInterceptor()
        httpLogger.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        when (includeJWT) {
            true -> {
                val jwtInterceptor = JWTInterceptor()
                OkHttpClient
                    .Builder()
                    .addInterceptor(httpLogger)
                    .addInterceptor(jwtInterceptor)
                    .build()
            }
            else -> {
                OkHttpClient
                    .Builder()
                    .addInterceptor(httpLogger)
                    .build()
            }
        }
    }

    protected val retrofit: Retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}

class JWTInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer ${AuthenticationStore.getJWT()}")
            .build()

        return chain.proceed(newRequest)
    }
}