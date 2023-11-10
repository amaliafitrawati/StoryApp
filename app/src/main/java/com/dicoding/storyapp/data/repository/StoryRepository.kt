package com.dicoding.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.api.ApiService
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.response.Story
import com.dicoding.storyapp.view.story.StoryRemoteMediator

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStories(token: String): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoryRemoteMediator(token, storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}