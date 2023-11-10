package com.dicoding.storyapp.view.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.view.login.LoginActivity
import com.dicoding.storyapp.view.main.MainViewModel
import com.dicoding.storyapp.view.signup.SignupActivity
import com.dicoding.storyapp.databinding.ActivityWelcomeBinding
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val viewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}