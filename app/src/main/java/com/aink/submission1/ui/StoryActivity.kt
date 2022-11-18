package com.aink.submission1.ui


import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aink.submission1.data.Result
import com.aink.submission1.databinding.ActivityStoryBinding
import com.aink.submission1.utils.UserPreference
import com.aink.submission1.utils.util.logout
import com.aink.submission1.utils.util.reduceFileImage
import com.aink.submission1.utils.util.rotateBitmap
import com.aink.submission1.utils.util.uploadDescription
import com.aink.submission1.utils.util.uploadImage
import com.aink.submission1.utils.util.uriToFile
import com.aink.submission1.viewmodel.AddStoryViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding

    private var getFile: File? = null
    private val addStoryViewModel: AddStoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSIONS)
        }

        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.galerryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener {
            addStory(getFile, (binding.edtDescription.text.trim().toString()))
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "izin ditolak", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"

        val chooser = Intent.createChooser(intent, "Pilih Gambar")
        launcherIntentGallery.launch(chooser)
    }

    private fun addStory(fileSource: File?, description: String) {
        if (((uploadImage(fileSource)).toString()).isNotEmpty()) {
            Toast.makeText(this@StoryActivity, uploadImage(fileSource).toString(), Toast.LENGTH_SHORT).show()
        }
        else if ((uploadDescription(description)).isNotEmpty()) {
            Toast.makeText(this@StoryActivity, uploadDescription(description), Toast.LENGTH_SHORT).show()
        }
        else {
            val file = reduceFileImage(fileSource!!)
            val requestDescription = description!!.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file!!.asRequestBody("image/jpeg".toMediaTypeOrNull())

            val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo", file.name, requestImageFile
            )

            val mUserPrefences = UserPreference(this)
            val token = mUserPrefences.getUser()
            if(token == null) {
                logout(this)
            }
            else {
                addStoryViewModel.postAddStory(requestDescription, imageMultiPart, token).observe(this) { result ->
                    if (result != null) {
                        when(result) {
                            is Result.Success -> {
                                finish()
                                Log.d("StoryActivity", result.data.message)
                            }
                            is Result.Error -> {
                                Toast.makeText(this@StoryActivity, result.error, Toast.LENGTH_SHORT).show()
                                Log.d("StoryActivity", result.error)
                            }
                        }
                    }
                }
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)

            binding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@StoryActivity)

            getFile = myFile

            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}