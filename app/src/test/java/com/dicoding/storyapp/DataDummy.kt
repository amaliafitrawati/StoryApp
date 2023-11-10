package com.dicoding.storyapp

import com.dicoding.storyapp.data.database.StoryItem
import com.dicoding.storyapp.response.Story

object DataDummy {

    fun generateResponseDummy() : List<Story>{
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "imgUrl + $i",
                "createdDate + $i",
                "name + $i",
                "desc + $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}