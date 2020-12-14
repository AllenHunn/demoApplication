package com.commentsolddemo.awesomestore

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object ESP {
    private lateinit var esp: SharedPreferences

    private const val prefName = "sharedPreferences"

    const val JWT = "JWT Token"

    fun initialize(context: Context) {
        context.let {
            val mka = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            esp = EncryptedSharedPreferences.create(
                prefName,
                mka,
                it,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    fun get(key: String, value: String?): String? {
        return esp.getString(key, value)
    }

    fun set(key: String, value: String?) {
        val prefsEditor: SharedPreferences.Editor = esp.edit()
        with(prefsEditor) {
            putString(key, value)
            commit()
        }
    }
}