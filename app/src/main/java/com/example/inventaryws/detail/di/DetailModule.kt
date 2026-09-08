package com.example.inventaryws.detail.di

import com.example.inventaryws.detail.data.repository.DetailRepositoryImpl
import com.example.inventaryws.detail.domain.repository.DetailRepository
import com.example.inventaryws.detail.domain.usecase.DeleteProductUseCase
import com.example.inventaryws.detail.domain.usecase.ObserveProductByIdUseCase
import com.example.inventaryws.detail.ui.DetailViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    singleOf(::DetailRepositoryImpl) { bind<DetailRepository>()}

    //casos de uso
    factoryOf(::ObserveProductByIdUseCase)
    factoryOf(::DeleteProductUseCase)

    //viewmodel
    viewModel { (productId:String) ->
        DetailViewModel(
            productId = productId,
            observeProductByIdUseCase = get(),
            deleteProductUseCase = get()
        )
    }
}