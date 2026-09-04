package com.example.inventaryws.core.domain.model

import java.util.Date

data class Product(
    val documentId : String,
    val name: String,
    val description: String,
    val category: String,
    val amount: Double,
    val stock: Int,
    val minimumStock: Int,
    val urlImg: String
)