package com.example.inventaryws.home.di

import com.example.inventaryws.home.data.repository.HomeRepositoryImpl
import com.example.inventaryws.home.domain.repository.HomeRepository
import com.example.inventaryws.home.domain.usecase.ObserveProductsUseCase
import com.example.inventaryws.home.ui.HomeViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    singleOf(::HomeRepositoryImpl){bind< HomeRepository>()}
    //domain
    factoryOf(::ObserveProductsUseCase)
    //ui
    viewModelOf(::HomeViewModel)
}