package com.dicoding.storyapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.storyapp.data.remote.Remote
import com.dicoding.storyapp.data.remote.RemoteDao
import com.dicoding.storyapp.data.remote.StoryDao
import com.dicoding.storyapp.response.Story

@Database(
    entities = [Story::class, Remote::class],
    version = 4,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteDao(): RemoteDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "story_database"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {INSTANCE = it}
            }
        }
    }
}