package com.example.inventaryws.detail.domain.repository

import com.example.inventaryws.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    fun observeProductById(productId: String) : Flow<Result<Product>>
    suspend fun deleteProduct(productId: String) : Result<Unit>
}