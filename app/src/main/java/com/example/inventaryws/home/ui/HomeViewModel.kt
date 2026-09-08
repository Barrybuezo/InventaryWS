package com.example.inventaryws.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventaryws.core.domain.model.Product
import com.example.inventaryws.home.domain.model.StockStatus
import com.example.inventaryws.home.domain.model.stockStatus
import com.example.inventaryws.home.domain.usecase.ObserveProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

const val CATEGORY_ALL = "Todos"

data class HomeUiState(
    val isLoading: Boolean = false,
    val allProducts: List<Product> = emptyList(), // lo que llega de Firebase, sin filtrar
    val searchQuery: String = "",
    val selectedCategory: String = CATEGORY_ALL,
    val error: String? = null
) {
    // Lista ya filtrada por texto de búsqueda + categoría, calculada al vuelo
    val filteredProducts: List<Product>
        get() = allProducts.filter { product ->
            val matchesCategory = selectedCategory == CATEGORY_ALL || product.category == selectedCategory
            val matchesSearch = product.name.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }

    val totalCount: Int get() = allProducts.size
    val availableCount: Int get() = allProducts.count { it.stockStatus() == StockStatus.NORMAL }
    val lowStockCount: Int get() = allProducts.count { it.stockStatus() == StockStatus.LOW }
}

class HomeViewModel(private val observeProductsUseCase: ObserveProductsUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeProducts()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onCategoryChanged(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    private fun observeProducts() {
        _uiState.update { it.copy(isLoading = true) }
        observeProductsUseCase()
            .onEach { result ->
                result.fold(
                    onSuccess = { products ->
                        _uiState.update { it.copy(isLoading = false, allProducts = products) }
                        Log.d("HomeViewModel", "observeProducts: $products")
                    },
                    onFailure = { e ->
                        _uiState.update { it.copy(isLoading = false, error = e.message) }
                        Log.e("HomeViewModel", "observeProducts: ${e.message}")
                    }
                )
            }.launchIn(viewModelScope)
    }
}