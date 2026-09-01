package com.example.inventaryws.core.firebase.di

import com.example.inventaryws.core.firebase.remote.FireBaseRemoteDataSource
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val firebaseModule = module {
    single<FirebaseFirestore> { Firebase.firestore }
    singleOf(::FireBaseRemoteDataSource)
}