package com.example.submission1intermediate.utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission1intermediate.data.remote.response.ListStoryItem
import com.example.submission1intermediate.databinding.StoriesRowBinding
import com.example.submission1intermediate.ui.activity.DetailStoryActivity

class HomeAdapter(): PagingDataAdapter<ListStoryItem, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoriesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        holder.binding.tvItemName.text = story?.name
        Glide.with(holder.binding.root)
            .load(story?.photoUrl)
            .into(holder.binding.ivItemPhoto)

        holder.itemView.setOnClickListener{
            val moveToDetailStory = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            moveToDetailStory.putExtra(DetailStoryActivity.STORY_ID, story?.id)
            holder.itemView.context.startActivity(moveToDetailStory)
        }
    }

    class ViewHolder(var binding: StoriesRowBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}