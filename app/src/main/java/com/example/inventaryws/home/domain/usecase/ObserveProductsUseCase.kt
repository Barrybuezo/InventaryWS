package com.example.inventaryws.home.domain.usecase

import com.example.inventaryws.home.domain.repository.HomeRepository

class ObserveProductsUseCase(private val homeRepository: HomeRepository) {
    operator fun invoke() = homeRepository.observeProducts()
}