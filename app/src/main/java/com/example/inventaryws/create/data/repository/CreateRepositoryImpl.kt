package com.example.inventaryws.create.data.repository

import com.example.inventaryws.core.domain.model.Product
import com.example.inventaryws.core.firebase.remote.FireBaseRemoteDataSource
import com.example.inventaryws.create.data.mapper.toDto
import com.example.inventaryws.create.domain.repository.CreateRepository

class CreateRepositoryImpl(private val firebase : FireBaseRemoteDataSource): CreateRepository {
    override suspend fun createProduct(product: Product): Result<Unit> = runCatching {
        //call firebase
        firebase.create(product.toDto())
    }
}