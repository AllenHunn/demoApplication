package com.commentsolddemo.awesomestore

object AuthenticationStore {
    private var jwt: String? = null

    fun getJWT(): String? {
        return this.jwt ?: {
            this.jwt = ESP.get(ESP.JWT, null)
            this.jwt
        }()
    }

    fun setJWT(jwt: String?) {
        ESP.set(ESP.JWT, jwt)
        this.jwt = jwt
    }
}