package com.example.submission1intermediate.ui.utils

import com.example.submission1intermediate.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryEntity(): List<ListStoryItem> {
        val storyList = ArrayList<ListStoryItem>()
        for (i in 0..6) {
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1687272833455_usC4ozpb.jpg",
                "2023-06-20T14:53:53.457Z",
                "Name $i",
                "Description",
                0,
                "story-uKkWVWMDD516c9qm",
                0,
            )
            storyList.add(story)
        }
        return storyList
    }
}