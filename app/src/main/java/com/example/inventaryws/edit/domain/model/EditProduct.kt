package com.example.inventaryws.edit.domain.model

import android.net.Uri

data class EditProduct(
    val documentId: String,
    val name: String,
    val category: String,
    val description: String,
    val amount: String,
    val stock: Int,
    val minimumStock: Int,
    val localImage: Uri?,
    val remoteImage: String,
)
