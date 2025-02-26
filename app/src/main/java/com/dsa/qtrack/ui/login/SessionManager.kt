package com.dsa.qtrack.ui.login

import android.content.Context
import android.util.Log
import com.dsa.qtrack.data.api.ApiClient

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("QTrackPrefs", Context.MODE_PRIVATE)

    fun saveSession(token: String, userId: Int, userName: String) {
        prefs.edit().apply {
            putString("TOKEN", token)
            putInt("USER_ID", userId)
            putString("USER_NAME", userName)
            apply()
        }
        Log.d("LoginDebug", "Guardado - TOKEN: $token, USER_ID: $userId, USER_NAME: $userName")
    }

    fun getUserName(): String? {
        val userName = prefs.getString("USER_NAME", null)
        Log.d("LoginDebug", "Recuperado USER_NAME: $userName")  // ✅ Verifica la recuperación
        return userName
    }


    fun isLoggedIn(): Boolean = !prefs.getString("TOKEN", null).isNullOrEmpty()

    fun getUserId(): Int = prefs.getInt("USER_ID", -1)

    fun logout() {
        prefs.edit().clear().apply()

        // Reinicia Retrofit para limpiar cualquier configuración de sesión
        ApiClient.resetClient()
    }


}
