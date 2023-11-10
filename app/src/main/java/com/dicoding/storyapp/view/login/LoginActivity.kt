package com.dicoding.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.helper.ViewModelFactory
import com.dicoding.storyapp.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        loginAnimation()

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.loginUser(email,password)
            viewModel.login.observe(this){
                if (!it.error!!) {
                    viewModel.saveSession(
                        UserModel(
                            it?.loginResult?.name.toString(),
                            "Bearer " + it?.loginResult?.token,
                            true
                        )
                    )
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loginAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val emailTitleTv =
            ObjectAnimator.ofFloat(binding.emailTitleTv, View.ALPHA, 1f).setDuration(100)
        val emailTv = ObjectAnimator.ofFloat(binding.emailLayoutTv, View.ALPHA, 1f).setDuration(100)
        val passTitleTv =
            ObjectAnimator.ofFloat(binding.passwordTitleTv, View.ALPHA, 1f).setDuration(100)
        val passTv =
            ObjectAnimator.ofFloat(binding.passwordTvLayout, View.ALPHA, 1f).setDuration(100)
        val btnLogin = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playTogether(emailTv, emailTitleTv, passTitleTv, passTv, btnLogin)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    companion object {
        private const val TAG = "User Login"
    }

}