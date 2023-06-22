package com.example.submission1intermediate.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.submission1intermediate.data.MyRepository
import com.example.submission1intermediate.data.local.LoginPreferences
import com.example.submission1intermediate.data.remote.retrofit.ApiConfig

object Injection {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_setting")
    fun provideRepository(context: Context): MyRepository {
        val apiService = ApiConfig.getApiService(context)
        val loginPreferences = LoginPreferences(context.dataStore)
        return MyRepository.getInstance(apiService, loginPreferences)
    }
}