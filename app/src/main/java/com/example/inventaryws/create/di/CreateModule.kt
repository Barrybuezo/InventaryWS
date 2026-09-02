package com.example.inventaryws.create.di

import com.example.inventaryws.create.data.repository.CreateRepositoryImpl
import com.example.inventaryws.create.domain.repository.CreateRepository
import com.example.inventaryws.create.domain.usecase.CreateDocumentUseCase
import com.example.inventaryws.ui.CreateViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val createModule = module {
    //data
    singleOf(::CreateRepositoryImpl){bind<CreateRepository>()}
    //domain
    factoryOf(::CreateDocumentUseCase)
    //ui
    viewModelOf(::CreateViewModel)
}