package com.example.inventaryws.create.domain.usecase

import com.example.inventaryws.core.domain.model.Product
import com.example.inventaryws.create.domain.repository.CreateRepository

class CreateDocumentUseCase(private val repository: CreateRepository) {
    suspend operator fun invoke(product: Product) = repository.createProduct(product)
}