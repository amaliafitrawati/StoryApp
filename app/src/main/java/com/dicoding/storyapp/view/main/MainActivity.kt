package com.dicoding.storyapp.view.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.helper.ViewModelFactory
import com.dicoding.storyapp.view.maps.MapsActivity
import com.dicoding.storyapp.view.story.AddStoryActivity
import com.dicoding.storyapp.view.story.StoryModelFactory
import com.dicoding.storyapp.view.story.StoryViewModel
import com.dicoding.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val storyViewModel by viewModels<StoryViewModel> {
        StoryModelFactory(this)
    }
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            val layoutManager = LinearLayoutManager(this)
            binding.recycleView.layoutManager = layoutManager
            val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
            binding.recycleView.addItemDecoration(itemDecoration)

            token = user.token
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }

            getData(token)

        }

        with(binding) {
            fab.setOnClickListener {
                val intentStory = Intent(this@MainActivity, AddStoryActivity::class.java)
                startActivity(intentStory)
            }
        }
        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun getData(token: String) {
        val adapter = StoryPagingAdapter()
        binding.recycleView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        storyViewModel.getStories(token).observe(this) {
            adapter.submitData(lifecycle, it)
            showLoading(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.location ->{
                startActivity(Intent(this, MapsActivity::class.java))
            }
            R.id.setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.logout -> {
                viewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}