package com.example.inventaryws.ui

import androidx.lifecycle.ViewModel
import com.example.inventaryws.create.domain.usecase.CreateDocumentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CreateUiState(
    val isLoading: Boolean = false,
    val name: String = "",
    val description : String = "",
    val amount : String = "",
    val stock : String = "",
    val minimumStock: String = "",
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

}