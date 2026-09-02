package com.example.inventaryws.create.domain.repository

import com.example.inventaryws.create.domain.model.CreateProduct

interface CreateRepository {
    suspend fun createProduct(product: CreateProduct): Result<Unit>

}