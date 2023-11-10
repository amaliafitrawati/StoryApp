package com.dicoding.storyapp.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.core.util.Pair
import com.dicoding.storyapp.databinding.StoryItemBinding
import com.dicoding.storyapp.response.Story
import com.dicoding.storyapp.view.story.DetailStoryActivity

class StoryPagingAdapter : PagingDataAdapter<Story, StoryPagingAdapter.MyViewHolder>(DIFF_CALLBACK){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        data?.let { holder.bind(it) }

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intentDetail.putExtra(DetailStoryActivity.DETAIL_STORY, data)
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                holder.itemView.context as Activity,
                Pair.create(holder.binding.imageView, "profile")
            )
            holder.itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
        }
    }
    class MyViewHolder(val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story){
            binding.nameTv.text = story.name
            binding.descTv.text = story.description
            Glide.with(this.itemView.context)
                .load(story.photoUrl)
                .into(binding.imageView)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}