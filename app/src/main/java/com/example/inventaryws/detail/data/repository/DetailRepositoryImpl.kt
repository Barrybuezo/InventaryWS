package com.example.inventaryws.detail.data.repository

import android.util.Log
import com.example.inventaryws.core.domain.model.Product
import com.example.inventaryws.core.firebase.remote.FireBaseRemoteDataSource
import com.example.inventaryws.core.supabase.remote.SupabaseRemoteDataSource
import com.example.inventaryws.detail.domain.repository.DetailRepository
import com.example.inventaryws.home.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class DetailRepositoryImpl(
    private val firebase: FireBaseRemoteDataSource,
    private val supabase: SupabaseRemoteDataSource
) : DetailRepository {
    override fun observeProductById(productId: String): Flow<Result<Product>> {
        return firebase.observeProduct(documentId = productId)
            .map { dto -> Result.success(dto.toDomain()) }
            .catch { e -> emit(Result.failure(e)) }
    }

    override suspend fun deleteProduct(productId: String, imgUrl: String): Result<Unit> =
        runCatching {
            if (imgUrl.isNotBlank()) {
                try {
                    supabase.deleteImage(imgUrl)
                } catch (e: Exception) {
                    Log.w(
                        "DetailRepositoryImpl",
                        "No se pudo borrar la imagen de Supabase: ${e.message}"
                    )
                }
            }
            firebase.delete(productId)
        }
}