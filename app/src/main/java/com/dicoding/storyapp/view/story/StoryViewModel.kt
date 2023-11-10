package com.dicoding.storyapp.view.story

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.di.StoryInjection
import com.dicoding.storyapp.response.Story

class StoryViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getStories(token: String): LiveData<PagingData<Story>> {
        return repository.getStories(token).cachedIn(viewModelScope)
    }
    companion object{
        private const val TAG = "Story View Model"
    }
}

class StoryModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(StoryInjection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}