package com.example.inventaryws.create.data.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun Context.createImageCaptureUri(): Uri {
    val imageDir = File(cacheDir, "images").apply { mkdirs() }
    val file = File(imageDir, "product_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(this,"$packageName.fileprovider", file)
}