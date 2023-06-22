package com.example.submission1intermediate.ui.viewmodel


import androidx.lifecycle.ViewModel
import com.example.submission1intermediate.data.MyRepository

class RegisterViewModel(private val repository: MyRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) = repository.registerUser(name, email, password)
}