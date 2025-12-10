package com.ebanisterialopez.ebanisterialopez.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.toProductUiModel
import com.ebanisterialopez.ebanisterialopez.domain.model.Product
import com.ebanisterialopez.ebanisterialopez.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
sealed class HomeState {
    object Loading : HomeState()
    data class Success(val products: List<Product>) : HomeState()
    data class Error(val message: String) : HomeState()
}
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val _uiState = mutableStateOf<HomeState>(HomeState.Loading)
    val uiState: State<HomeState> = _uiState
    init {
        fetchFeaturedProducts()
    }
    private fun fetchFeaturedProducts() {
        viewModelScope.launch {
            _uiState.value = HomeState.Loading
            try {
                val apiData = repository.getFeaturedProducts()
                val mappedProducts = apiData.map { it.toProductUiModel() }
                _uiState.value = HomeState.Success(mappedProducts)
            } catch (e: Exception) {
                _uiState.value = HomeState.Error("Fallo en la API: ${e.message}")
            }
        }
    }

}