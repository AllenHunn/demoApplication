package com.commentsolddemo.awesomestore.handlers

import com.commentsolddemo.awesomestore.AuthenticationResult
import okhttp3.Credentials
import retrofit2.http.GET
import retrofit2.http.Header

interface AuthenticationService {
    @GET("status")
    suspend fun authenticate(@Header("Authorization") credentials: String): AuthenticationResult

}

class AuthenticationHandler : BaseHandler() {
    suspend fun authenticate(email: String, password: String): AuthenticationResult? {

        val service = retrofit.create(AuthenticationService::class.java)
        val credentials = Credentials.basic(email, password)
        return service.authenticate(credentials)
    }
}
