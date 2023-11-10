package com.dicoding.storyapp.view.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.data.repository.Repository
import com.dicoding.storyapp.response.AddStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val userRepository: Repository) : ViewModel(){
    private val _story = MutableLiveData<AddStoryResponse>()
    val story: LiveData<AddStoryResponse> = _story

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postStory(token: String, image: MultipartBody.Part, desc: RequestBody) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postStory(token, image, desc)
        Log.d(TAG, "postStory: $client")
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(call: Call<AddStoryResponse>, response: Response<AddStoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _story.value = response.body()
                    Log.d(TAG, "onResponse: ${_story.value}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    _story.value = AddStoryResponse(true, errorMessage.toString())
                    Log.e(TAG, "onFailure: $errorMessage")
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _story.value = AddStoryResponse( true, t.message.toString())
                Log.e(TAG, "onFailure2: ${t.message}")
            }

        })
    }

    companion object{
        private const val TAG = "Add Story"
    }

}