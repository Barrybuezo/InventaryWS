package com.example.inventaryws.edit.domain.model

data class Product(
    val documentId: String,
    val name: String,
    val description: String,
    val category: String,
    val amount: Double,
    val stock: Int,
    val minimumStock: Int,
    val imgUrl: String
)