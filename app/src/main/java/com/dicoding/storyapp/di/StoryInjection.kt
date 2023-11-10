package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.repository.StoryRepository

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}