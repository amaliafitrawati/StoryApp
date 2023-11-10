package com.dicoding.storyapp.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.data.repository.Repository
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.response.LoginResponse
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val userRepository: Repository) : ViewModel(){
    private val _login = MutableLiveData<LoginResponse>()
    val login: LiveData<LoginResponse> = _login

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveSession(user :UserModel){
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _login.value = response.body()
                    Log.d(TAG, "onResponse: ${_login.value}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    _login.value = LoginResponse(null, true, errorMessage.toString())
                    Log.e(TAG, "onFailure: $errorMessage")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _login.value = LoginResponse(null, true, t.message)
                Log.e(TAG, "onFailure2: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "User Login"
    }

}