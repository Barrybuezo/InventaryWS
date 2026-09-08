package com.example.inventaryws.edit.domain.repository

import com.example.inventaryws.edit.domain.model.EditProduct
import com.example.inventaryws.edit.domain.model.Product


interface EditRepository {
    suspend fun getProductById(documentId: String) : Result<Product>

    suspend fun upDateProduct(product: EditProduct): Result<Unit>
}