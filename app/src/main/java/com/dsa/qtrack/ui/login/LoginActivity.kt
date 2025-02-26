package com.dsa.qtrack.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dsa.qtrack.R
import com.dsa.qtrack.data.api.ApiClient
import com.dsa.qtrack.data.api.QtrackApiService
import com.dsa.qtrack.model.LoginRequest
import com.dsa.qtrack.model.LoginResponse
import com.dsa.qtrack.ui.home.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        // ✅ Reinicia Retrofit para limpiar cualquier instancia previa (especialmente después del logout)
        ApiClient.resetClient()

        if (sessionManager.isLoggedIn()) {
            navigateToHome()
            return
        }

        setContentView(R.layout.activity_login)
        initViews()

        btnLogin.setOnClickListener { performLogin() }
    }

    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (!validateInputs(email, password)) return

        showLoading(true)

        val apiService = ApiClient.retrofit.create(QtrackApiService::class.java)
        apiService.login(LoginRequest(email, password)).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        Log.d("LoginDebug", "Respuesta recibida: ${loginResponse.user.nombre}")
                        sessionManager.saveSession(
                            loginResponse.token,
                            loginResponse.user.id,
                            loginResponse.user.nombre ?: "Sin Nombre"
                        )
                        navigateToHome()
                    }
                } else {
                    Log.e("LoginDebug", "Error en login: ${response.errorBody()?.string()}")
                    Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                val errorMessage = when (t) {
                    is SocketTimeoutException -> "Error: Tiempo de conexión agotado."
                    is UnknownHostException -> "Error: No se pudo conectar al servidor."
                    else -> "Error inesperado: ${t.localizedMessage}"
                }

                Log.e("LoginDebug", "Error en la solicitud: ${t.localizedMessage}", t)
                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validateInputs(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                etEmail.error = "El correo es obligatorio"
                false
            }
            password.isEmpty() -> {
                etPassword.error = "La contraseña es obligatoria"
                false
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnLogin.isEnabled = !isLoading
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
