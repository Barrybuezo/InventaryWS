package com.example.inventaryws.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventaryws.home.domain.model.Product
import com.example.inventaryws.home.domain.usecase.ObserveProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String ?= null
)
class HomeViewModel(private val observeProductsUseCase: ObserveProductsUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState : StateFlow<HomeUiState> = _uiState.asStateFlow()


    init {
        observeProducts()
    }

    private fun observeProducts() {
        _uiState.update { it.copy(isLoading = true) }
        observeProductsUseCase()
            .onEach { result ->
                result.fold(
                    onSuccess = { products ->
                        //cargar productos
                        _uiState.update { it.copy(isLoading = false, products = products) }
                        Log.d("HomeViewModel", "observeProducts: $products")
                    },
                    onFailure = {e ->
                        _uiState.update { it.copy(isLoading = false, error = e.message) }
                        Log.e("HomeViewModel", "observeProducts: ${e.message}")
                    }
                )
            } .launchIn(viewModelScope)
    }
}
