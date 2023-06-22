package com.example.submission1intermediate.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.submission1intermediate.databinding.ActivityAddStoryBinding
import com.example.submission1intermediate.ui.viewmodel.AddStoryViewModel
import com.example.submission1intermediate.utils.ViewModelFactory
import com.example.submission1intermediate.utils.bitmapToMultipart
import com.example.submission1intermediate.utils.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel by viewModels<AddStoryViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    private var bitmap : Bitmap? = null
    private val cameraPermission = android.Manifest.permission.CAMERA
    private val storagePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val mediaPermission = android.Manifest.permission.ACCESS_MEDIA_LOCATION

    companion object{
        const val USER_TOKEN = "user_token"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGallery.setOnClickListener { dispatchTakePictureIntent() }
        binding.buttonAdd.setOnClickListener { uploadImage() }

    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap
            bitmap = imageBitmap
            Glide.with(binding.previewImageView).load(imageBitmap).into(binding.previewImageView)
        }
    }
    private val documentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        val contentResolver = this.contentResolver
        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        bitmap = imageBitmap
        Glide.with(binding.previewImageView).load(imageBitmap).into(binding.previewImageView)
    }

    private fun dispatchTakePictureIntent() {
        val pickImage = "Pick Image"
        val takePhoto = "Take Photo"

        val options = arrayOf<CharSequence>(pickImage, takePhoto)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Image Source")
        builder.setItems(options) { _, item ->
            when (options[item]) {
                pickImage -> {
                    if (ContextCompat.checkSelfPermission(this, storagePermission) != PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(this ,mediaPermission)) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(storagePermission,mediaPermission),
                            4
                        )
                    } else {
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        documentLauncher.launch(intent.type)
                    }
                }
                takePhoto -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            cameraPermission
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(cameraPermission),
                            6
                        )
                    } else {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        resultLauncher.launch(intent)
                    }
                }
            }
        }
        builder.show()
    }

    private fun uploadImage() {
        if (bitmap != null) {
            val image = bitmapToMultipart(bitmap!!)

            val token = intent.getStringExtra(USER_TOKEN).toString()
            val description = binding.edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())
            viewModel.postStory(token, image, description).observe(this){result->
                if (result != null){
                    when(result){
                        is Result.Loading -> showLoading(true)
                        is Result.Error -> showLoading(false)
                        is Result.Success -> {
                            showLoading(false)
                            finish()
                        }
                    }
                }
            }


        } else {
            Toast.makeText(this, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBarAddStory.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}