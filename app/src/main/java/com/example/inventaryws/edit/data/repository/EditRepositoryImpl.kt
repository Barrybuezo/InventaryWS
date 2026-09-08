package com.example.inventaryws.edit.data.repository

import android.util.Log
import com.example.inventaryws.core.firebase.remote.FireBaseRemoteDataSource
import com.example.inventaryws.core.supabase.remote.SupabaseRemoteDataSource
import com.example.inventaryws.edit.data.mapper.toDomain
import com.example.inventaryws.edit.data.mapper.toDto
import com.example.inventaryws.edit.domain.model.EditProduct
import com.example.inventaryws.edit.domain.model.Product
import com.example.inventaryws.edit.domain.repository.EditRepository


class EditRepositoryImpl(
    private val firebase: FireBaseRemoteDataSource,
    private val supabase: SupabaseRemoteDataSource
) : EditRepository {
    override suspend fun getProductById(documentId: String): Result<Product> {
        return runCatching {
            val product = firebase.getProductById(documentId).toDomain()
            product
        }
    }

    override suspend fun upDateProduct(product: EditProduct): Result<Unit> {
        return runCatching {
            val imageUrl = if (product.localImage != null) {
                val pathSupabase = product.remoteImage

                Log.d("EditRepositoryImpl", "updateProduct: $pathSupabase")
                //eliminar imagen de supabase
                supabase.deleteImage(pathSupabase)

                //subir nuevo uri
                val newUrl = supabase.uploadImagen(product.localImage)
                newUrl
            } else {
                product.remoteImage
            }

            firebase.updateDocument(product.copy(remoteImage = imageUrl).toDto())
        }
    }
}