package com.travoro.app.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.graphics.scale
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File.createTempFile
import java.io.FileOutputStream

fun uriToMultipart(
    context: Context,
    uri: Uri,
): MultipartBody.Part {
    val inputStream = context.contentResolver.openInputStream(uri)
    val originalBitmap = BitmapFactory.decodeStream(inputStream)

    val maxSize = 800
    val ratio = minOf(
        maxSize.toFloat() / originalBitmap.width,
        maxSize.toFloat() / originalBitmap.height,
    )
    val width = (originalBitmap.width * ratio).toInt()
    val height = (originalBitmap.height * ratio).toInt()

    val resizedBitmap = originalBitmap.scale(width, height)

    val file = createTempFile("Profile", ".jpg")

    FileOutputStream(file).use { output ->
        resizedBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            80,
            output,
        )
    }
    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(
        name = "profilePic",
        filename = file.name,
        requestFile,
    )
}
