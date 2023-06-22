package com.example.submission1intermediate.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.submission1intermediate.data.remote.response.Story
import com.example.submission1intermediate.databinding.ActivityDetailStoryBinding
import com.example.submission1intermediate.ui.viewmodel.DetailStoryViewModel
import com.example.submission1intermediate.utils.ViewModelFactory
import com.example.submission1intermediate.utils.Result

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var token: String
    private val viewModel: DetailStoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        const val STORY_ID = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val userId = intent.getStringExtra(STORY_ID).toString()
        viewModel.token.observe(this) {
            token = it
            viewModel.getDetailStory(token, userId).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Error -> showLoading(false)
                        is Result.Success -> {
                            showLoading(false)
                            val story = result.data.story
                            setDetailStory(story)
                        }
                    }
                }
            }
        }
    }

    private fun setDetailStory(story: Story) {
        binding.tvDetailName.text = story.name
        supportActionBar?.title = story.name
        binding.tvDetailDescription.text = story.description
        Glide.with(binding.root)
            .load(story.photoUrl)
            .into(binding.ivDetailPhoto)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}