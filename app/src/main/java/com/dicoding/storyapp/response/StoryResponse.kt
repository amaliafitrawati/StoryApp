package com.dicoding.storyapp.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(
	@SerializedName("listStory")
	val listStory: List<Story>,

	@SerializedName("error")
	val error: Boolean,

	@SerializedName("message")
	val message: String
)

@Parcelize
@Entity(tableName = "story")
data class Story(
	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
) : Parcelable

data class AddStoryResponse(
	@SerializedName("error")
	val error: Boolean,
	@SerializedName("message")
	val message: String
)
