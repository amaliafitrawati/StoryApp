package com.dicoding.storyapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivitySignupBinding
import com.dicoding.storyapp.helper.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        signupAnimation()

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            viewModel.register(name,email, pass)
            viewModel.signup.observe(this) {
                AlertDialog.Builder(this).apply {
                    setMessage(it?.message)
                    if (!it.error!!) {
                        setTitle(R.string.regist_success_alert)
                        setPositiveButton("Continue") { _, _ ->
                            finish()
                        }
                    }else{
                        setTitle(R.string.regist_failed_alert)
                        setPositiveButton("Ok") { _, _ ->
                            finish()
                        }
                    }
                    create()
                    show()
                }
            }
        }
    }

    private fun signupAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val nameTitleTv =
            ObjectAnimator.ofFloat(binding.nameTitleTv, View.ALPHA, 1f).setDuration(100)
        val nameTv = ObjectAnimator.ofFloat(binding.nameTvLayout, View.ALPHA, 1f).setDuration(100)
        val emailTitleTv =
            ObjectAnimator.ofFloat(binding.emailTitleTv, View.ALPHA, 1f).setDuration(100)
        val emailTv = ObjectAnimator.ofFloat(binding.emailTvLayout, View.ALPHA, 1f).setDuration(100)
        val passTitleTv =
            ObjectAnimator.ofFloat(binding.passwordTitleTv, View.ALPHA, 1f).setDuration(100)
        val passTv =
            ObjectAnimator.ofFloat(binding.passTvLayout, View.ALPHA, 1f).setDuration(100)
        val btnSignup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(nameTv, nameTitleTv, emailTv, emailTitleTv, passTitleTv, passTv, btnSignup)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }
}