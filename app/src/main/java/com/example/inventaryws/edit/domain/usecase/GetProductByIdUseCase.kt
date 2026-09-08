package com.example.inventaryws.edit.domain.usecase

import com.example.inventaryws.edit.domain.repository.EditRepository

class GetProductByIdUseCase(private val repository: EditRepository) {
    suspend operator fun invoke(documentId: String) = repository.getProductById(documentId)
}