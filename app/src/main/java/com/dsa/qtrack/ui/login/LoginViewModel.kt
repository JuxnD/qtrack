package com.dsa.qtrack.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsa.qtrack.data.datastore.TokenDataStore
import com.dsa.qtrack.data.model.User
import com.dsa.qtrack.data.repository.LoginRepository
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
                val response = repository.login(email, password)
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        tokenDataStore.saveToken(loginResponse.token)
                        _state.value = LoginState.Success(loginResponse.user, loginResponse.token)
                    } ?: run {
                        _state.value = LoginState.Error("Respuesta vacía del servidor.")
                    }
                } else {
                    _state.value = LoginState.Error("Error: ${response.message()}")
                }
            } catch (e: HttpException) {
                _state.value = LoginState.Error("Error HTTP: ${e.localizedMessage}")
            } catch (e: Exception) {
                _state.value = LoginState.Error("Error desconocido: ${e.localizedMessage}")
            }
        }
    }
}
