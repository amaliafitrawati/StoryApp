package com.dicoding.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.repository.Repository
import com.dicoding.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: Repository) : ViewModel(){

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    companion object{
        private const val TAG = "Main View Model"
    }
}