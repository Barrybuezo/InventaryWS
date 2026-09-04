package com.example.inventaryws.create.data.mapper

import com.example.inventaryws.core.firebase.dto.ProductDto
import com.example.inventaryws.create.domain.model.CreateProduct

fun CreateProduct.toDto() = ProductDto(
    name = name,
    category = category,
    amount = amount.toDouble(),
    stock = stock,
    minimumStock = minimumStock,
    urlImg = null
)