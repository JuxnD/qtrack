package com.dsa.qtrack.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsa.qtrack.data.model.User
import com.dsa.qtrack.data.repository.LoginRepository
import com.dsa.qtrack.data.datastore.TokenDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
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
                    val user = response.body()?.user
                    val token = response.body()?.token

                    if (user != null && token != null) {
                        tokenDataStore.saveToken(token)
                        _state.value = LoginState.Success(user)
                    } else {
                        _state.value = LoginState.Error("Datos inválidos")
                    }
                } else {
                    _state.value = LoginState.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _state.value = LoginState.Error("Error: ${e.localizedMessage}")
            }
        }
    }
}