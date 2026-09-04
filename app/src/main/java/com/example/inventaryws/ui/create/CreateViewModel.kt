package com.example.inventaryws.ui.create

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val description : String = "",
    val amount : String = "",
    val stock : String = "",
    val minimumStock: String = "",
    val localUri: Uri = Uri.EMPTY,
    val content: CreateContent = CreateContent.Idle
)

sealed interface CreateContent {
    data object Idle : CreateContent
    data object Success : CreateContent
    data class Error(val message: String) : CreateContent
}

class CreateViewModel(private val createDocumentUseCase: CreateDocumentUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateUiState())
    val uiState: StateFlow<CreateUiState> = _uiState.asStateFlow()

    fun onNameChanged(value: String){
        _uiState.update { product -> product.copy(name = value) }
    }

    fun onDescriptionChanged(value: String){
        _uiState.update { product -> product.copy(description = value) }
    }

    fun onAmountChanged(value: String){
        _uiState.update { product -> product.copy(amount = value) }
    }

    fun onStockChanged(value: String){
        _uiState.update { product -> product.copy(stock = value) }
    }

    fun onMinimumStockChanged(value: String){
        _uiState.update { product -> product.copy(minimumStock = value) }
    }

    fun onImageSelected(uri : Uri){
        _uiState.update { it.copy(localUri = uri) }
    }

    fun saveProduct(){
        _uiState.update { product -> product.copy(isLoading = true) } //para que servia copy

        //validaciones

        val newProduct = CreateProduct(
            name = _uiState.value.name,
            category = "Test",
            description = _uiState.value.description,
            amount = _uiState.value.amount,
            stock = _uiState.value.stock.toInt(),
            minimumStock = _uiState.value.minimumStock.toInt(),
            imageUri = _uiState.value.localUri
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
                            content = CreateContent.Error(message = exception.message?:"Unknown error")//pq no confundirme?
                        )
                    }
                }
            )
        }
    }
}