package com.example.inventaryws.detail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventaryws.core.domain.model.Product
import com.example.inventaryws.detail.domain.usecase.ObserveProductByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class DetailUiState(
    val isLoading: Boolean = false,
    val detail: DetailEvent = DetailEvent.Idle
)

sealed interface DetailEvent {
    data object Idle: DetailEvent
    data class Detail(val productDetail: Product) : DetailEvent
    data class Error(val msg: String) : DetailEvent
}

class DetailViewModel(
    private val productId: String,
    private val observeProductByIdUseCase: ObserveProductByIdUseCase
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
}