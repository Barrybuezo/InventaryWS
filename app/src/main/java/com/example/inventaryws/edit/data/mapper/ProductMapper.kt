package com.example.inventaryws.edit.data.mapper

import com.example.inventaryws.core.firebase.dto.ProductDto
import com.example.inventaryws.edit.domain.model.EditProduct
import com.example.inventaryws.edit.domain.model.Product

fun ProductDto.toDomain() = Product(
    documentId = documentId,
    name = name ?: "Empty Data",
    description = description?: "Empty Data",
    category = category ?: "Empty Data",
    amount = amount ?: 0.0,
    stock = stock ?: 0,
    minimumStock = minimumStock ?: 0,
    imgUrl = urlImg ?: "Empty Data"
)

fun EditProduct.toDto() = ProductDto(
    documentId = documentId,
    name = name,
    category = category,
    description = description,
    amount = amount.toDouble(),
    stock = stock,
    minimumStock = minimumStock,
    urlImg = remoteImage
)