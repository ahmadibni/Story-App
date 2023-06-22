package com.example.submission1intermediate.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submission1intermediate.data.MyRepository
import com.example.submission1intermediate.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MyRepository) : ViewModel() {
    val tokenKu = repository.tokenKu

    fun deleteSession() {
        viewModelScope.launch {
            repository.deleteSession()
        }
    }

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> =
        repository.getAllStories(token).cachedIn(viewModelScope)
}