package com.example.inventaryws.core.supabase.di

import com.example.inventaryws.core.supabase.remote.SupabaseRemoteDataSource
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val supabaseModule = module{
    single {
        createSupabaseClient(
            supabaseKey = "sb_publishable_-VYWMLahu87Gy4lC5pGuow_8VJ_ki-X",
            supabaseUrl = "https://unelnvoahtzuddczoxiq.supabase.co",
        ){
            install(Storage)
        }
    }

    singleOf(::SupabaseRemoteDataSource)
}