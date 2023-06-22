package com.example.submission1intermediate.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.submission1intermediate.data.local.LoginPreferences
import com.example.submission1intermediate.data.remote.response.*
import com.example.submission1intermediate.data.remote.retrofit.ApiService
import com.example.submission1intermediate.utils.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MyRepository private constructor(
    private val apiService: ApiService,
    private val loginPreferences: LoginPreferences
    ){
    fun postStory(
        token : String,
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postStory(token,file, description)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Error(e.toString()))
        }
    }

    fun logUser(
        email: String,
        password: String,
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.logUser(email, password)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun registerUser(
        name: String,
        email: String,
        password: String,
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.registerUser(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Error(e.toString()))
        }
    }

    fun getAllStories(
        token: String
    ): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                PagingSource(apiService, token)
            }
        ).liveData
    }

    fun getDetailStory(
        token: String,
        id: String,
    ): LiveData<Result<DetailStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStory(token, id)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Error(e.toString()))
        }
    }

    fun getMapStories(
        token: String
    ): LiveData<Result<StoryMapResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getMapStories(token)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Error(e.toString()))
        }
    }

    suspend fun saveSession(token: String, isLogin: Boolean, username: String){
        loginPreferences.saveSession(token, isLogin, username)
    }

    suspend fun deleteSession(){
        loginPreferences.deleteSession()
    }
    val session = loginPreferences.getSession().asLiveData()

    val tokenKu = loginPreferences.getUserToken().asLiveData()

    companion object{
        @Volatile
        private var instance: MyRepository?= null
        fun getInstance(
            apiService: ApiService,
            loginPreferences: LoginPreferences
        ): MyRepository =
            instance ?: synchronized(this){
                instance ?: MyRepository(apiService, loginPreferences)
            }.also { instance = it}
    }
}