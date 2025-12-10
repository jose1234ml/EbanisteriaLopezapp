package com.ebanisterialopez.ebanisterialopez.presentation.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.domain.model.Product
import com.ebanisterialopez.ebanisterialopez.domain.repository.ProductRepository
import kotlinx.coroutines.launch
sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}