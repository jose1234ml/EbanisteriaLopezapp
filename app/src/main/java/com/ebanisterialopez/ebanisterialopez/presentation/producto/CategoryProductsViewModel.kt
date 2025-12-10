package com.ebanisterialopez.ebanisterialopez.presentation.producto

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.domain.model.Product
import com.ebanisterialopez.ebanisterialopez.domain.repository.ProductRepository
import com.ebanisterialopez.ebanisterialopez.presentation.model.CategoryProductsState
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.toDomain // <-- si tu extension se llama toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
private const val CATEGORY_NAME_KEY = "categoryName"
@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var uiState by mutableStateOf<CategoryProductsState>(CategoryProductsState.Loading)
        private set
    private val rawCategoryName: String? = savedStateHandle.get<String>(CATEGORY_NAME_KEY)
    val categoryName: String = rawCategoryName?.trim().takeIf { !it.isNullOrBlank() } ?: ""
    init {
        if (categoryName.isNotBlank()) {
            loadProductsByCategory(categoryName)
        } else {
            uiState = CategoryProductsState.Error("Categoría no especificada")
        }
    }
    private fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            uiState = CategoryProductsState.Loading
            try {
                val dtoList = repository.getProductsByCategory(category)
                val domainList: List<Product> = dtoList.map { it.toDomain() }
                uiState = CategoryProductsState.Success(domainList)

            } catch (e: Exception) {
                uiState = CategoryProductsState.Error("Error al cargar productos: ${e.localizedMessage ?: "Desconocido"}")
            }
        }
    }
}