package com.example.inventaryws.edit.domain.usecase

import com.example.inventaryws.edit.domain.model.EditProduct
import com.example.inventaryws.edit.domain.repository.EditRepository

class UpdateProductUseCase(private val repository: EditRepository) {
    suspend operator fun invoke(product: EditProduct) = repository.upDateProduct(product)
}