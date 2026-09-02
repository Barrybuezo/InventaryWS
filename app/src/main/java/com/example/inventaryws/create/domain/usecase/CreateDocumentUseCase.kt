package com.example.inventaryws.create.domain.usecase

import com.example.inventaryws.create.domain.model.CreateProduct
import com.example.inventaryws.create.domain.repository.CreateRepository

class CreateDocumentUseCase(private val repository: CreateRepository) {
    suspend operator fun invoke(product: CreateProduct) = repository.createProduct(product)
}