package com.example.inventaryws.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CreateUiState(
    val isLoading: Boolean = false,
    val content: CreateContent = CreateContent.Idle
)

sealed interface CreateContent {
    data object Idle : CreateContent
    data object Success : CreateContent
    data class Error(val message: String) : CreateContent
}

class CreateViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CreateUiState())
    val uiState: StateFlow<CreateUiState> = _uiState.asStateFlow()
}