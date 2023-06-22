package com.example.submission1intermediate.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1intermediate.R
import com.example.submission1intermediate.data.remote.response.ListStoryItem
import com.example.submission1intermediate.databinding.ActivityHomeBinding
import com.example.submission1intermediate.ui.viewmodel.HomeViewModel
import com.example.submission1intermediate.utils.HomeAdapter
import com.example.submission1intermediate.utils.LoadingStateAdapter
import com.example.submission1intermediate.utils.ViewModelFactory
import com.example.submission1intermediate.utils.Result

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var token: String
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.tokenKu.observe(this) {
            token = it
            setStories()
        }

        supportActionBar?.title = "Story"

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                viewModel.deleteSession()
                finish()
            }
            R.id.action_add_story -> {
                val moveToAddStoryActivity = Intent(this, AddStoryActivity::class.java)
                moveToAddStoryActivity.putExtra(AddStoryActivity.USER_TOKEN, token)
                startActivity(moveToAddStoryActivity)
            }
            R.id.action_map -> {
                val moveToMapActivity = Intent(this, MapsActivity::class.java)
                moveToMapActivity.putExtra(MapsActivity.USER_TOKEN, token)
                startActivity(moveToMapActivity)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onBackPressed() {}

    private fun setStories() {
        val adapter = HomeAdapter()
        binding.rvHomeStory.layoutManager = LinearLayoutManager(this)
        binding.rvHomeStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        viewModel.getStories(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
}