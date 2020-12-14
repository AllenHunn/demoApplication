package com.commentsolddemo.awesomestore

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.commentsolddemo.awesomestore.handlers.AuthenticationHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ESP.initialize(applicationContext)

        val job = Job()
        val coroutineScope = CoroutineScope(job + Dispatchers.Main)

        coroutineScope.launch {
            Constants.init()
        }

        AuthenticationStore.getJWT()?.let {
            goToMainActivity()
            return
        }
        setContentView(R.layout.activity_login)
    }

    fun btnLogin_OnClick(view: View): Unit {
        val email = findViewById<EditText>(R.id.textEmailAddress).text.toString()
        val password = findViewById<EditText>(R.id.textPassword).text.toString()

        val authenticationHandler =
            AuthenticationHandler()
        val job = Job()

        val coroutineScope = CoroutineScope(job + Dispatchers.Main)
        coroutineScope.launch {
            val authenticationResult = authenticationHandler.authenticate(email, password)
            authenticationResult?.let {
                when (it.error) {
                    0 -> {
                        AuthenticationStore.setJWT(it.token)
                        goToMainActivity()
                    }
                    else -> {
                        Toast.makeText(
                            applicationContext,
                            "Error logging in. Please try again. Error Code: ${it.error}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun goToMainActivity(): Unit {
        val intent = Intent(this, ItemListActivity::class.java)
        startActivity(intent)
    }
}