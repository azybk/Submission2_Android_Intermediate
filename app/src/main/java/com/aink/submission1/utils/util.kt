package com.aink.submission1.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.MenuItem
import com.aink.submission1.R
import com.aink.submission1.ui.MainActivity
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


object util {

    private const val FILENAME_FORMAT = "dd-MMM-yyyy"

    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    fun checkNama(nama: String): String {
        return if (nama.isEmpty())
            "isi nama"
        else
            ""
    }

    fun checkEmail(email: String): String {
        return if (email.isEmpty())
            "isi email"
        else
            ""
    }

    fun checkPasswordLength(password: String): String {
       return if (password.isEmpty() || password.length < 6)
           "password tidak boleh Kurang dari 6"
        else
            ""
    }

    fun selectItem(context: Context, item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                logout(context)
                return true
            }
            else -> return false
        }
    }

    fun logout(context: Context) {
        val mUserPreference = UserPreference(context)
        mUserPreference.hapusUser(context)

        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

        }
    }

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    fun createFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        return File(outputDirectory, "$timeStamp.jpg")
    }

    fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
        val matrix = Matrix()
        return if (isBackCamera) {
            matrix.postRotate(90f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        } else {
            matrix.postRotate(-90f)
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int

        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun uploadImage(file: File?): String? {
        return if(file == null) {
            "Pilih gambar/photo terlebih dahulu"
        } else {
            ""
        }
    }

    fun uploadDescription(description: String): String {
        return if (description?.isEmpty()) {
            "masukan deskripsi terlebih dahulu"
        } else {
            ""
        }
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)

            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5

        } while (streamLength > 1000000)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
    }

}