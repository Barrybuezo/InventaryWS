package com.example.inventaryws.core.firebase.remote

import com.example.inventaryws.core.firebase.dto.ProductDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FireBaseRemoteDataSource(
    private val firestore: FirebaseFirestore
) {
    //insert
    suspend fun create(productDto: ProductDto) {
        firestore
            .collection("Productos")
            .add(productDto)
            .await()
    }

    //select
    fun observeProducts(): Flow<List<ProductDto>> {
        return callbackFlow {
            val registration = firestore
                .collection("Productos")
                .addSnapshotListener { snapshots, error ->
                    if(error != null){
                        close(error)
                        return@addSnapshotListener
                    }

                    if(snapshots == null){
                        trySend(emptyList())
                        return@addSnapshotListener
                    }

                    val products = try {
                        snapshots.toObjects(ProductDto::class.java)
                    }catch (e: Exception){
                        close(e)
                        return@addSnapshotListener
                    }

                    trySend(products)
                }

            awaitClose {
                registration.remove()
            }
        }
    }
}