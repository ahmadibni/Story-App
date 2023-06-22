package com.example.submission1intermediate.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.submission1intermediate.data.MyRepository

class MapViewModel(private val repository: MyRepository): ViewModel() {

    fun getMapStories(token: String) = repository.getMapStories(token)

}