package com.example.inventaryws.detail.data.repository

import com.example.inventaryws.core.domain.model.Product
import com.example.inventaryws.core.firebase.remote.FireBaseRemoteDataSource
import com.example.inventaryws.detail.domain.repository.DetailRepository
import com.example.inventaryws.home.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class DetailRepositoryImpl(private val firebase: FireBaseRemoteDataSource) : DetailRepository {
    override fun observeProductById(productId: String): Flow<Result<Product>> {
        return firebase.observeProduct(documentId = productId)
            .map { dto -> Result.success(dto.toDomain()) }
            .catch { e -> emit(Result.failure(e)) }
    }

    override suspend fun deleteProduct(productId: String): Result<Unit> = runCatching {
        firebase.delete(productId)
    }
}