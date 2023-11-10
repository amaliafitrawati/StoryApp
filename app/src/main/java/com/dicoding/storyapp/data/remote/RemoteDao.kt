package com.dicoding.storyapp.data.remote

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remote: List<Remote>)

    @Query("SELECT * FROM remote WHERE id = :id")
    fun getRemoteId(id: String): Remote?

    @Query("DELETE FROM remote")
    fun deleteRemote()
}