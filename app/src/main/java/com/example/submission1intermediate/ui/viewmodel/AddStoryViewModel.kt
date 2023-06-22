package com.example.submission1intermediate.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.submission1intermediate.data.MyRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: MyRepository): ViewModel() {

    fun postStory(token: String, file: MultipartBody.Part, description: RequestBody) =
        repository.postStory(token, file, description)
}