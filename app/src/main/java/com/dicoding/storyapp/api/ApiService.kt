package com.dicoding.storyapp.api

import com.dicoding.storyapp.response.AddStoryResponse
import com.dicoding.storyapp.response.LoginResponse
import com.dicoding.storyapp.response.RegisterResponse
import com.dicoding.storyapp.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
     fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getStory(
        @Header("Authorization") token: String
    ): Call<StoryResponse>

    @GET("stories")
    suspend fun getStoryPaging(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ) : StoryResponse

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>

    @GET("stories")
    fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location : Int = 1,
    ): Call<StoryResponse>
}