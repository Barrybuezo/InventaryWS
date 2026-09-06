package com.example.inventaryws.home.domain.model

data class Product(
    val documentId: String,
    val name: String,
    val category: String,
    val amount: String,
    val stock: Int,
    val minimumStock: Int,
    val imgUrl: String
)
