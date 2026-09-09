package com.example.inventaryws.detail.domain.usecase

import com.example.inventaryws.detail.domain.repository.DetailRepository

class DeleteProductUseCase(private val repository: DetailRepository) {
    suspend operator fun invoke(productId: String, imgUrl: String) = repository.deleteProduct(productId, imgUrl)
}