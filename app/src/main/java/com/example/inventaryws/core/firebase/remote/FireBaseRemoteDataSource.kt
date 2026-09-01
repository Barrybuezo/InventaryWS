package com.example.inventaryws.core.firebase.remote

import com.example.inventaryws.core.firebase.dto.ProductDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FireBaseRemoteDataSource(
    private val firestore: FirebaseFirestore
) {
    suspend fun create(productDto: ProductDto) {
        firestore
            .collection("Productos")
            .add(productDto)
            .await()
    }
}