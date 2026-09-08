package com.example.inventaryws.detail.domain.usecase

import com.example.inventaryws.detail.domain.repository.DetailRepository

class ObserveProductByIdUseCase(private val repository: DetailRepository) {
    operator fun invoke(productId: String) = repository.observeProductById(productId)
}