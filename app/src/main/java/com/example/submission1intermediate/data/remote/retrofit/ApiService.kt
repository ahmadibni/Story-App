package com.example.submission1intermediate.data.remote.retrofit

import com.example.submission1intermediate.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") token : String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : AddStoryResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun logUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token : String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): StoriesResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header("Authorization") token : String,
        @Path("id") id: String
    ): DetailStoryResponse

    @GET("stories?location=1")
    suspend fun getMapStories(
        @Header("Authorization") token : String
    ): StoryMapResponse
}