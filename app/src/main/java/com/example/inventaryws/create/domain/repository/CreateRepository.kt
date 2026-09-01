package com.example.inventaryws.create.domain.repository

import com.example.inventaryws.core.domain.model.Product

interface CreateRepository {
    suspend fun createProduct(product: Product): Result<Unit>

}