package com.example.inventaryws.create.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventaryws.core.domain.model.CreateProductCategory
import com.example.inventaryws.create.domain.model.CreateProduct
import com.example.inventaryws.create.domain.usecase.CreateDocumentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateUiState(
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
    val content: CreateContent = CreateContent.Idle
)

sealed interface CreateContent {
    data object Idle : CreateContent
    data object Success : CreateContent
    data class Error(val message: String) : CreateContent
}

//validacion de campo vacio
private fun requiredFieldError(value: String): String? =
    if (value.isBlank()) "Campo requerido" else null

//validacion del precio
private fun positiveAmountError(value: String): String? {
    requiredFieldError(value)?.let { return it }
    val parsed = value.toDoubleOrNull() ?: return "Ingresa un número válido"
    return if (parsed <= 0.0) "Debe ser ayor a 0" else null
}

//validacion del stock
private fun nonNegativeIntError(value: String): String?{
    requiredFieldError(value)?.let { return it }
    val parsed = value.toIntOrNull() ?: return "Ingresa un número entero válido"
    return if (parsed < 0) "No puede ser negativo" else null
}

class CreateViewModel(private val createDocumentUseCase: CreateDocumentUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateUiState())
    val uiState: StateFlow<CreateUiState> = _uiState.asStateFlow()

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

    fun saveProduct() {
        _uiState.update { product -> product.copy(isLoading = true) } //para que servia copy

        //validaciones

        val formFields = _uiState.value
        val newProduct = CreateProduct(
            name = formFields.name,
            description = formFields.description,
            category = formFields.category.label,
            amount = formFields.amount,
            stock = formFields.stock.toInt(),
            minimumStock = formFields.minimumStock.toInt(),
            imageUri = formFields.localUri
        )

        viewModelScope.launch {
            createDocumentUseCase(product = newProduct).fold(
                onSuccess = {
                    _uiState.update {
                        CreateUiState(
                            content = CreateContent.Success
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            content = CreateContent.Error(
                                message = exception.message ?: "Unknown error"
                            )//pq no confundirme?
                        )
                    }
                }
            )
        }
    }
}