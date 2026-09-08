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
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    if (snapshots == null) {
                        trySend(emptyList())
                        return@addSnapshotListener
                    }

                    val products = try {
                        snapshots.toObjects(ProductDto::class.java)
                    } catch (e: Exception) {
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

    fun observeProduct(documentId: String): Flow<ProductDto> {
        return callbackFlow {
            val registration = firestore.collection("Productos")
                .document(documentId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    if (snapshot == null || !snapshot.exists()) {
                        close(NoSuchElementException("Documento con id $documentId no encontrado"))
                        return@addSnapshotListener
                    }

                    val product = snapshot.toObject(ProductDto::class.java)

                    if (product != null) {
                        trySend(product)
                    }
                }

            awaitClose {
                registration.remove()
            }
        }
    }

    suspend fun getProductById(documentId: String): ProductDto {
        val snapshot = firestore
            .collection("Productos")
            .document(documentId)
            .get()
            .await()

        return snapshot.toObject(ProductDto::class.java)
            ?: throw NoSuchElementException("Documento con id $documentId no encontrado")
    }

    suspend fun updateDocument(product: ProductDto){
        val update = mapOf(
            "name" to product.name,
            "category" to product.category,
            "description" to product.description,
            "amount" to product.amount,
            "stock" to product.stock,
            "minimumStock" to product.minimumStock,
            "urlImg" to product.urlImg
        )

        firestore
            .collection("Productos")
            .document(product.documentId)
            .update(update)
            .await()
    }

    suspend fun delete(documentId: String) {
        firestore
            .collection("Productos")
            .document(documentId)
            .delete()
            .await()
    }
}