package com.example.submission1intermediate.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submission1intermediate.data.MyRepository

class DetailStoryViewModel(private val repository: MyRepository) : ViewModel() {
    val token = repository.tokenKu


    fun getDetailStory(token: String, userId: String) = repository.getDetailStory(token, userId)
}