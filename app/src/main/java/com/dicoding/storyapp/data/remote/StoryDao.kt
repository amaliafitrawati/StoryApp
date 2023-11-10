package com.dicoding.storyapp.data.remote

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.storyapp.response.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story: List<Story>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, Story>

    @Query("DELETE FROM story")
    fun deleteAll() : Integer
}