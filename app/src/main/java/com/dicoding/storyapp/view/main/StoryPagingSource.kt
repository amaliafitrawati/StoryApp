package com.dicoding.storyapp.view.main

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.storyapp.api.ApiService
import com.dicoding.storyapp.response.Story

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, Story>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            return LoadResult.Page(emptyList(), 0, 1)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return 0
    }

    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }
}