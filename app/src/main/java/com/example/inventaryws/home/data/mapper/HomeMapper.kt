package com.example.inventaryws.home.data.mapper

import com.example.inventaryws.core.firebase.dto.ProductDto
import com.example.inventaryws.core.domain.model.Product

fun ProductDto.toDomain() = Product(
    documentId = documentId ?: "Empty Data",
    name = name ?: "Empty Data",
    category = category ?: "Empty Data",
    amount = amount ?: 0.0,
    stock = stock ?: 0,
    minimumStock = minimumStock ?: 0,
    imgUrl = urlImg ?: "Empty Data"
)