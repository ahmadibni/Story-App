package com.example.submission1intermediate.ui.viewmodel

import androidx.lifecycle.*
import com.example.submission1intermediate.data.MyRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MyRepository) : ViewModel() {
    val session = repository.session

    fun saveSession(token: String, isLogin: Boolean, username: String) {
        viewModelScope.launch {
            repository.saveSession(token, isLogin, username)
        }
    }

    fun logUser(email: String, password: String) = repository.logUser(email, password)
}