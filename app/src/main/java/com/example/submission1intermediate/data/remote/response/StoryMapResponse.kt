package com.example.submission1intermediate.data.remote.response

import com.google.gson.annotations.SerializedName

data class StoryMapResponse(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryMapItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class ListStoryMapItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("lon")
	val lon: Any,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("lat")
	val lat: Any
)
