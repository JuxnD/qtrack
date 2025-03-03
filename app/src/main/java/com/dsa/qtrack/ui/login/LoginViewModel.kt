package com.dsa.qtrack.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsa.qtrack.data.datastore.TokenDataStore
import com.dsa.qtrack.data.model.User
import com.dsa.qtrack.data.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User, val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(
    private val repository: LoginRepository,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                Log.d("LoginDebug", "Iniciando sesión con Email=$email")

                val response = repository.login(email, password)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Log.d("LoginDebug", "Login exitoso. Usuario: ${loginResponse.user.nombre}, Token: ${loginResponse.token}")

                        // Guardar el token en el DataStore
                        tokenDataStore.saveToken(loginResponse.token)
                        _state.value = LoginState.Success(loginResponse.user, loginResponse.token)
                    } else {
                        Log.e("LoginDebug", "Error: Respuesta vacía del servidor")
                        _state.value = LoginState.Error("El servidor no devolvió datos válidos.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LoginDebug", "Error en login: Código HTTP=${response.code()}, Mensaje=${response.message()}, Detalles=$errorBody")
                    _state.value = LoginState.Error("Error en la autenticación: ${response.message()} (Detalles: $errorBody)")
                }
            } catch (e: HttpException) {
                Log.e("LoginDebug", "Error HTTP: ${e.code()} - ${e.message()}")
                _state.value = LoginState.Error("Error en el servidor: ${e.message()}")
            } catch (e: IOException) {
                Log.e("LoginDebug", "Error de conexión: ${e.localizedMessage}")
                _state.value = LoginState.Error("No se pudo conectar al servidor. Verifica tu conexión a Internet.")
            } catch (e: Exception) {
                Log.e("LoginDebug", "Error desconocido: ${e.localizedMessage}", e)
                _state.value = LoginState.Error("Error inesperado: ${e.localizedMessage}")
            }
        }
    }
}
