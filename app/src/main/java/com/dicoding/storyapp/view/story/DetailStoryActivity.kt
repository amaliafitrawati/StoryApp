package com.dicoding.storyapp.view.story

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.dicoding.storyapp.databinding.ActivityDetailStoryBinding
import com.dicoding.storyapp.response.Story

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private var story: Story? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        story = if(Build.VERSION.SDK_INT >= 33) intent.getParcelableExtra(DETAIL_STORY, Story::class.java)
                else intent.getParcelableExtra(DETAIL_STORY)

        binding.nameTv.text = "${story?.name}"
        binding.descTv.text = "${story?.description}"
        Glide.with(this)
            .load("${story?.photoUrl}")
            .into(binding.imgStoryDetail)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        const val DETAIL_STORY = "DETAIL_STORY"
    }

}