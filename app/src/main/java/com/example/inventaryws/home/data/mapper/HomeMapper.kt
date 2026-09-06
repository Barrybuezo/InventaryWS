package com.example.inventaryws.home.data.mapper

import com.example.inventaryws.core.firebase.dto.ProductDto
import com.example.inventaryws.home.domain.model.Product

fun ProductDto.toDomain() = Product(
    documentId = documentId?: "Empty Data",
    name = name ?: "Empty Data",
    category = category ?: "Empty Data",
    amount = amount.toString(),
    stock = stock ?: 0,
    minimumStock = minimumStock ?: 0,
    imgUrl = urlImg ?: "Empty Data"
)