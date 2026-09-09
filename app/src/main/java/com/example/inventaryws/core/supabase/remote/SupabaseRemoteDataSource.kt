package com.example.inventaryws.core.supabase.remote

import android.net.Uri
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload

class SupabaseRemoteDataSource(supabaseClient: SupabaseClient) {
    private val bucket = supabaseClient.storage.from(bucketId = "productos")

    //subir imagenes
    suspend fun uploadImagen(uri: Uri) : String{
        val path = "GT02/GT02_${System.currentTimeMillis()}.jpg"

        bucket.upload(path = path,uri)

        return bucket.publicUrl(path = path)
    }

    suspend fun deleteImage(url : String){
        val path = url.substringAfter("productos/")
        bucket.delete(path)
    }
}