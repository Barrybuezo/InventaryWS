package com.example.inventaryws.home.domain.repository

import com.example.inventaryws.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    //contrato para traer informacion desde firestore
    fun observeProducts(): Flow<Result<List<Product>>>
}