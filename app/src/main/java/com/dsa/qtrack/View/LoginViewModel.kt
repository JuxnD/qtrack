package com.dsa.qtrack.View

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsa.qtrack.model.LoginRequest
import com.dsa.qtrack.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User, val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = ApiClient.api.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    response.body()?.let {
                        _loginState.value = LoginState.Success(it.user, it.token)
                    } ?: run {
                        _loginState.value = LoginState.Error("Respuesta vacía.")
                    }
                } else {
                    _loginState.value = LoginState.Error("Error: ${response.message()}")
                }
            } catch (e: HttpException) {
                _loginState.value = LoginState.Error("Excepción: ${e.localizedMessage}")
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error desconocido: ${e.localizedMessage}")
            }
        }
    }
}
