package com.example.inventaryws.home.data.repository

import com.example.inventaryws.core.firebase.remote.FireBaseRemoteDataSource
import com.example.inventaryws.home.data.mapper.toDomain
import com.example.inventaryws.home.domain.model.Product
import com.example.inventaryws.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class HomeRepositoryImpl(private val firebase: FireBaseRemoteDataSource): HomeRepository {
    override fun observeProducts(): Flow<Result<List<Product>>> {
        return firebase.observeProducts()
            .map { dtos -> Result.success(dtos.map { it.toDomain()}) }
            .catch { e -> emit(Result.failure(e)) }
    }
}