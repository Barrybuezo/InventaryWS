package com.example.inventaryws.edit.di

import com.example.inventaryws.edit.data.repository.EditRepositoryImpl
import com.example.inventaryws.edit.domain.repository.EditRepository
import com.example.inventaryws.edit.domain.usecase.GetProductByIdUseCase
import com.example.inventaryws.edit.domain.usecase.UpdateProductUseCase
import com.example.inventaryws.edit.ui.EditViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val editModule = module {
    //data
    singleOf(::EditRepositoryImpl) { bind<EditRepository>()}
    //domain
    factoryOf(::GetProductByIdUseCase)
    factoryOf(::UpdateProductUseCase)
    //ui
    viewModel {(productId:String) ->
        EditViewModel(
            productId = productId,
            getProductByIdUseCase = get(),
            updateProductUseCase = get()
        )
    }


}