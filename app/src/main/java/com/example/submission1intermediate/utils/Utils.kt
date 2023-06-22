package com.example.submission1intermediate.utils

import android.graphics.Bitmap
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*

fun bitmapToMultipart(bitmap: Bitmap): MultipartBody.Part {
    val tempFile = File.createTempFile("tempImage", ".jpg")
    val bitmapStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapStream)

    FileOutputStream(tempFile).use { out ->
        out.write(bitmapStream.toByteArray())
    }

    val file = File(tempFile.path)
    val requestFile = file.asRequestBody("image/jpeg".toMediaType())
    return MultipartBody.Part.createFormData("photo", file.name, requestFile)
}