package com.dicoding.storyapp.view.story

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dicoding.storyapp.api.ApiService
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.remote.Remote
import com.dicoding.storyapp.response.Story

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val token: String,
    private val database: StoryDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, Story>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getStoryPaging(token, page, state.config.pageSize)
            val endOfPaginationReached = responseData.listStory.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteDao().deleteRemote()
                    database.storyDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.listStory.map {
                    Remote(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                keys.let { database.remoteDao().insertAll(it) }
                database.storyDao().insertStory(responseData.listStory)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>): Remote? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteDao().getRemoteId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>): Remote? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteDao().getRemoteId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Story>): Remote? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteDao().getRemoteId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
