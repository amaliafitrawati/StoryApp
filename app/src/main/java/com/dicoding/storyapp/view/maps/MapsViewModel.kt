package com.dicoding.storyapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.response.Story
import com.dicoding.storyapp.response.StoryResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel() : ViewModel() {
    private val _story = MutableLiveData<List<Story>>()
    val story: LiveData<List<Story>> = _story

    fun getStoryLocation(token : String){
        val client = ApiConfig.getApiService().getStoriesWithLocation(token, LOCATION_STATUS)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    _story.value = response.body()?.let { (it.listStory) }
                    Log.d(TAG, "onResponse: ${_story.value}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    Log.e(TAG, "onResponse: $errorMessage")
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object{
        private const val TAG = "Maps View Model"
        private const val LOCATION_STATUS = 1
    }

}