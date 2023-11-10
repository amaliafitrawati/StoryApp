package com.dicoding.storyapp.view.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityAddStoryBinding
import com.dicoding.storyapp.helper.reduceFileImage
import com.dicoding.storyapp.helper.uriToFile
import com.dicoding.storyapp.helper.ViewModelFactory
import com.dicoding.storyapp.view.camera.CameraActivity
import com.dicoding.storyapp.view.main.MainActivity
import com.dicoding.storyapp.view.main.MainViewModel
import com.dicoding.storyapp.view.welcome.WelcomeActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddStoryBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val storyViewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnGallery.setOnClickListener {startGallery()}
        binding.btnCamera.setOnClickListener {startCamera()}
        binding.btnUpload.setOnClickListener {uploadImage()}
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgStory.setImageURI(it)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherCamera.launch(intent)
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == CAMERA_X_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERA_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun uploadImage() {
        viewModel.getSession().observe(this) {
            if (!it.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                currentImageUri?.let { uri ->
                    val imgFile = uriToFile(uri, this).reduceFileImage()
                    val requestImgFile = imgFile.asRequestBody("image/jpeg".toMediaType())
                    val imgMultipart: MultipartBody.Part =
                        MultipartBody.Part.createFormData("photo", imgFile.name, requestImgFile)
                    val description = binding.desc.text.toString().toRequestBody("text/plain".toMediaType())

                    if (!binding.desc.text.toString().isNullOrBlank()) {
                        storyViewModel.postStory(it.token, imgMultipart, description)
                        storyViewModel.story.observe(this) {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.desc_empty_alert), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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

    companion object {
        const val CAMERA_X_RESULT = 200
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}