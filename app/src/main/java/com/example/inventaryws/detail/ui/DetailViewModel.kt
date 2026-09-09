package com.example.inventaryws.detail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventaryws.core.domain.model.Product
import com.example.inventaryws.detail.domain.usecase.DeleteProductUseCase
import com.example.inventaryws.detail.domain.usecase.ObserveProductByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val detail: DetailEvent = DetailEvent.Idle
)

sealed interface DetailEvent {
    data object Idle: DetailEvent
    data class Detail(val productDetail: Product) : DetailEvent
    data object Deleted: DetailEvent
    data class Error(val msg: String) : DetailEvent
}

class DetailViewModel(
    private val productId: String,
    private val observeProductByIdUseCase: ObserveProductByIdUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState(isLoading = true))
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        observeProduct()
    }

    private fun observeProduct() {
        observeProductByIdUseCase(productId)
            .onEach { result ->
                result.fold(
                    onSuccess = { product ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                detail = DetailEvent.Detail(product)
                            )
                        }
                    },
                    onFailure = { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                detail = DetailEvent.Error(e.message ?: "Unknow Error")
                            )
                        }
                    }
                )
            }.launchIn(viewModelScope)
    }

    fun deleteProduct(){
        val currentProduct = (uiState.value.detail as? DetailEvent.Detail)?.productDetail ?: return

        _uiState.update { it.copy(isDeleting = true) }
        viewModelScope.launch {
            deleteProductUseCase(currentProduct.documentId, currentProduct.imgUrl).fold(
                onSuccess = {
                    _uiState.update { it.copy(isDeleting = false, detail = DetailEvent.Deleted) }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(isDeleting = false, detail = DetailEvent.Error(e.message ?: "Unknow Error"))
                    }

                }
            )
        }
    }

}