package com.example.submission1intermediate.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginPreferences (private val dataStore: DataStore<Preferences>) {

    fun getSession(): Flow<Boolean> {
        return dataStore.data.map {
            it[SESSION_KEY] ?: false
        }
    }
    fun getUserToken(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN_KEY] ?: ""
        }
    }

    suspend fun saveSession(token: String, islogin: Boolean, username: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
            it[SESSION_KEY] =islogin
            it[USERNAME_KEY] = username
        }
    }

    suspend fun deleteSession() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token_key")
        private val USERNAME_KEY = stringPreferencesKey("username_key")
        private val SESSION_KEY = booleanPreferencesKey("session_key")
    }
}