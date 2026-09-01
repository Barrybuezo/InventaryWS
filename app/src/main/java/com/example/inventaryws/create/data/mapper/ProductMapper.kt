package com.example.inventaryws.create.data.mapper

import com.example.inventaryws.core.domain.model.Product
import com.example.inventaryws.core.firebase.dto.ProductDto
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
import kotlin.String

fun Product.toDto() = ProductDto(
    name = name,
    category = category,
    amount = amount,
    stock = stock,
    minimumStock = minimumStock,
    urlImg = urlImg
)