package com.example.inventaryws.edit.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventaryws.core.domain.model.CreateProductCategory
import com.example.inventaryws.create.domain.model.CreateProduct
import com.example.inventaryws.edit.domain.model.EditProduct
import com.example.inventaryws.edit.domain.usecase.GetProductByIdUseCase
import com.example.inventaryws.edit.domain.usecase.UpdateProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class EditUiState(
    val isLoading: Boolean = false,
    val name: String = "",
    val description: String = "",
    val category: CreateProductCategory = CreateProductCategory.DEFAULT,
    val amount: String = "",
    val stock: String = "",
    val minimumStock: String = "",
    val localUri: Uri = Uri.EMPTY,
    val remoteImgUrl: String = "",
    val content: EditContent = EditContent.Idle
)

sealed interface EditContent {
    data object Idle : EditContent
    data object Success : EditContent
    data object Edited: EditContent
    data class Error(val message: String) : EditContent
}

class EditViewModel(
    private val productId: String,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditUiState())
    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()


    init {
        loadProductInfo()
    }

    private fun loadProductInfo() = viewModelScope.launch {

        getProductByIdUseCase(productId).fold(
            onSuccess = { product ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        name = product.name,
                        category = CreateProductCategory.entries.find {
                            it.name.equals(
                                product.category,
                                ignoreCase = true
                            )
                        }
                            ?: CreateProductCategory.DEFAULT,
                        description = product.description,
                        amount = product.amount.toString(),
                        stock = product.stock.toString(),
                        minimumStock = product.minimumStock.toString(),
                        remoteImgUrl = product.imgUrl,
                        content = EditContent.Success
                    )
                }
            },
            onFailure = { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        content = EditContent.Error(e.message ?: "Unknow Error")
                    )
                }
            }
        )
    }

    fun onNameChanged(value: String) {
        _uiState.update { product -> product.copy(name = value) }
    }

    fun onDescriptionChanged(value: String) {
        _uiState.update { product -> product.copy(description = value) }
    }

    fun onCategory(value: CreateProductCategory) {
        _uiState.update { it.copy(category = value) }
    }

    fun onAmountChanged(value: String) {
        _uiState.update { product -> product.copy(amount = value) }
    }

    fun onStockChanged(value: String) {
        _uiState.update { product -> product.copy(stock = value) }
    }

    fun onMinimumStockChanged(value: String) {
        _uiState.update { product -> product.copy(minimumStock = value) }
    }

    fun onImageSelected(uri: Uri) {
        _uiState.update { it.copy(localUri = uri) }
    }

    fun saveChanges() = viewModelScope.launch {
        _uiState.update { product -> product.copy(isLoading = true) }

        //validaciones

        val formFields = _uiState.value
        val newProduct = EditProduct(
            documentId = productId,
            name = formFields.name,
            description = formFields.description,
            category = formFields.category.label,
            amount = formFields.amount,
            stock = formFields.stock.toInt(),
            minimumStock = formFields.minimumStock.toInt(),
            localImage = formFields.localUri,
            remoteImage = formFields.remoteImgUrl
        )

        //guardar
        updateProductUseCase(newProduct).fold(
            onSuccess = {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        content = EditContent.Edited
                    )
                }
            },
            onFailure = { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        content = EditContent.Error(e.message ?: "Unknow Error")
                    )
                }
            }
        )
    }
}