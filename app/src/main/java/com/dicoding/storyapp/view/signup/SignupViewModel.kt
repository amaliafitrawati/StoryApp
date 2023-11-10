package com.dicoding.storyapp.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.data.repository.Repository
import com.dicoding.storyapp.response.RegisterResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel(private val userRepository: Repository) : ViewModel() {
    private val _signup = MutableLiveData<RegisterResponse>()
    val signup: LiveData<RegisterResponse> = _signup

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, pass: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(name, email, pass)
        client.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>,
                response: Response<RegisterResponse>) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    Log.e(TAG, "onSuccess: ${response.body()?.message}")
                    _signup.value = RegisterResponse(false, response.body()?.message)
                } else {
                    val body = response.errorBody()?.string()
                    val msg = body?.let { JSONObject(body).getString("message") }
                    Log.e(TAG, "onFailure: ${msg}")
                    _signup.value = RegisterResponse(true, msg)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _signup.value = RegisterResponse(true, t.message)
                Log.e(TAG, "onFailure2: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "Signup View Model"
    }
}

