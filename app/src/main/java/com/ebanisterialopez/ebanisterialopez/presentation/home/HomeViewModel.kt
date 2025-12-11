package com.ebanisterialopez.ebanisterialopez.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.toProductUiModel
import com.ebanisterialopez.ebanisterialopez.domain.model.Product
import com.ebanisterialopez.ebanisterialopez.domain.repository.ProductRepository
import com.ebanisterialopez.ebanisterialopez.presentation.model.HomeIntent
import com.ebanisterialopez.ebanisterialopez.presentation.model.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState(isLoading = true))
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        onIntent(HomeIntent.LoadFeaturedProducts)
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadFeaturedProducts -> fetchFeaturedProducts()
        }
    }

    private fun fetchFeaturedProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            try {
                val apiData = repository.getFeaturedProducts()
                val products = apiData.map { it.toProductUiModel() }
                _state.value = _state.value.copy(isLoading = false, products = products)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Fallo en la API: ${e.message ?: "Desconocido"}"
                )
            }
        }
    }
}