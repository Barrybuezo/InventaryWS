package com.example.inventaryws.create.data.repository

import com.example.inventaryws.core.firebase.remote.FireBaseRemoteDataSource
import com.example.inventaryws.core.supabase.remote.SupabaseRemoteDataSource
import com.example.inventaryws.create.data.mapper.toDto
import com.example.inventaryws.create.domain.model.CreateProduct
import com.example.inventaryws.create.domain.repository.CreateRepository

class CreateRepositoryImpl(
    private val firebase: FireBaseRemoteDataSource,
    private val supabase: SupabaseRemoteDataSource
) : CreateRepository {
    override suspend fun createProduct(product: CreateProduct): Result<Unit> = runCatching {
        val productDto = product.toDto()
        //upload image
        val publicUrl = supabase.uploadImagen(product.imageUri)

        //call firebase
        firebase.create(productDto.copy(urlImg = publicUrl))
    }
}