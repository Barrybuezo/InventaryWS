package com.example.inventaryws.create.domain.model

import android.net.Uri

data class CreateProduct(
    val name: String,
    val category: String,
    val description: String,
    val amount: String,
    val stock: Int,
    val minimumStock: Int,
    val imageUri: Uri
)
