package com.example.inventaryws.edit.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventaryws.core.domain.model.CreateProductCategory
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
    val nameError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val category: CreateProductCategory = CreateProductCategory.DEFAULT,
    val categoryError: String? = null,
    val amount: String = "",
    val amountError: String? = null,
    val stock: String = "",
    val stockError: String? = null,
    val minimumStock: String = "",
    val minimumStockError: String? = null,
    val localUri: Uri = Uri.EMPTY,
    val remoteImgUrl: String = "",
    val content: EditContent = EditContent.Idle
)

sealed interface EditContent {
    data object Idle : EditContent
    data object Success : EditContent
    data object Edited : EditContent
    data class Error(val message: String) : EditContent
}


//validacion de campo vacio
private fun requiredFieldError(value: String): String? =
    if (value.isBlank()) "Campo requerido" else null

//validacion del precio
private fun positiveAmountError(value: String): String? {
    requiredFieldError(value)?.let { return it }
    val parsed = value.toDoubleOrNull() ?: return "Ingresa un número válido"
    return if (parsed <= 0.0) "Debe ser mayor a 0" else null
}

//validacion del stock
private fun nonNegativeIntError(value: String): String? {
    requiredFieldError(value)?.let { return it }
    val parsed = value.toIntOrNull() ?: return "Ingresa un número entero válido"
    return if (parsed < 0) "No puede ser negativo" else null
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
        _uiState.update { product ->
            product.copy(
                name = value,
                nameError = requiredFieldError(value)
            )
        }
    }

    fun onDescriptionChanged(value: String) {
        _uiState.update { product ->
            product.copy(
                description = value,
                descriptionError = requiredFieldError(value)
            )
        }
    }

    fun onCategory(value: CreateProductCategory) {
        _uiState.update { it.copy(category = value, categoryError = null) }
    }

    fun onAmountChanged(value: String) {
        _uiState.update { product ->
            product.copy(
                amount = value,
                amountError = positiveAmountError(value)
            )
        }
    }

    fun onStockChanged(value: String) {
        _uiState.update { product ->
            product.copy(
                stock = value,
                stockError = nonNegativeIntError(value)
            )
        }
    }

    fun onMinimumStockChanged(value: String) {
        _uiState.update { product ->
            product.copy(
                minimumStock = value,
                minimumStockError = nonNegativeIntError(value)
            )
        }
    }

    fun onImageSelected(uri: Uri) {
        _uiState.update { it.copy(localUri = uri) }
    }

    fun saveChanges() = viewModelScope.launch {
        _uiState.update { current ->
            current.copy(
                nameError = requiredFieldError(current.name),
                categoryError = if (current.category == CreateProductCategory.DEFAULT) "Selecciona una categoria" else null,
                descriptionError = requiredFieldError(current.description),
                amountError = positiveAmountError(current.amount),
                stockError = nonNegativeIntError(current.stock),
                minimumStockError = nonNegativeIntError(current.minimumStock)
            )
        }

        val formFields = _uiState.value

        val hasErrors = listOf(
            formFields.nameError,
            formFields.categoryError,
            formFields.descriptionError,
            formFields.amountError,
            formFields.stockError,
            formFields.minimumStockError
        ).any { it != null }

        if(hasErrors)

        _uiState.update { product -> product.copy(isLoading = true) }

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