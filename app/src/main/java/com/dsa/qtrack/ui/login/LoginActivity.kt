package com.dsa.qtrack.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dsa.qtrack.MainActivity
import com.dsa.qtrack.R
import com.dsa.qtrack.api.ApiClient
import com.dsa.qtrack.data.Api.QtrackApiService
import com.dsa.qtrack.model.LoginRequest
import com.dsa.qtrack.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        // 🔑 Si ya hay sesión activa, redirige automáticamente al MainActivity
        if (checkSession()) {
            Log.d("LoginActivity", "Sesión activa encontrada. Redirigiendo al MainActivity.")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener { performLogin() }

    }




    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        Log.d("LoginActivity", "Iniciando login con email: $email")

        val loginRequest = LoginRequest(email, password)
        val apiService = ApiClient.retrofit.create(QtrackApiService::class.java)

        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("LoginActivity", "Login exitoso. Token recibido: ${loginResponse?.token}")

                    loginResponse?.token?.let { token ->
                        saveSessionToken(token)
                        Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        Log.d("LoginActivity", "Redirigiendo a MainActivity")
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } ?: run {
                        Log.w("LoginActivity", "Respuesta exitosa pero sin token.")
                        Toast.makeText(this@LoginActivity, "Error al procesar la respuesta.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.w("LoginActivity", "Credenciales incorrectas o error en la respuesta: ${response.code()}")
                    Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("LoginActivity", "Error en la solicitud de login: ${t.localizedMessage}", t)
                Toast.makeText(this@LoginActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveSessionToken(token: String) {
        val sharedPreferences = getSharedPreferences("QTrackPrefs", MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("TOKEN", token)
            apply()
        }
        Log.d("LoginActivity", "Token guardado en SharedPreferences.")
    }

    // ✅ Verifica si existe un token en las preferencias para mantener sesión activa
    private fun checkSession(): Boolean {
        val sharedPreferences = getSharedPreferences("QTrackPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)
        Log.d("LoginActivity", "Token encontrado en sesión: $token")
        return !token.isNullOrEmpty()
    }
}
