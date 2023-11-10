package com.dicoding.storyapp.view.story

import com.dicoding.storyapp.api.ApiService
import com.dicoding.storyapp.data.database.StoryItem
import com.dicoding.storyapp.response.AddStoryResponse
import com.dicoding.storyapp.response.LoginResponse
import com.dicoding.storyapp.response.RegisterResponse
import com.dicoding.storyapp.response.Story
import com.dicoding.storyapp.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class FakeApiService : ApiService {
    override fun register(name: String, email: String, password: String): Call<RegisterResponse> {
        TODO("Not yet implemented")
    }

    override fun login(email: String, password: String): Call<LoginResponse> {
        TODO("Not yet implemented")
    }

    override fun getStory(token: String): Call<StoryResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getStoryPaging(token: String, page: Int, size: Int): StoryResponse {
        val storyItem: MutableList<StoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryItem(
                i.toString(),
                "imgUrl + $i",
                "createdDate + $i",
                "name + $i",
                "desc + $i",
                i.toDouble(),
                i.toDouble()
            )
            storyItem.add(quote)
        }
        val start = (page - 1) * size
        val end = start + size
        val storyList = if (start > storyItem.size) emptyList()
                        else storyItem.subList(start, end.coerceAtMost(storyItem.size))

        val listStoryItem = storyList.map{data ->
            Story(
                data.photoUrl!!,
                data.createdAt!!,
                data.name!!,
                data.description!!,
                data.id,
                data.lon,
                data.lat
            ) }.toList()
        return StoryResponse(listStoryItem, false,"SUCCESS GET DATA")
    }

    override fun postStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Call<AddStoryResponse> {
        TODO("Not yet implemented")
    }

    override fun getStoriesWithLocation(token: String, location: Int): Call<StoryResponse> {
        TODO("Not yet implemented")
    }

}