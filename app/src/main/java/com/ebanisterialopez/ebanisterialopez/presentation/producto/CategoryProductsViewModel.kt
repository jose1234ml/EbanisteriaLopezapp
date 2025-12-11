package com.ebanisterialopez.ebanisterialopez.presentation.producto

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.domain.model.Product
import com.ebanisterialopez.ebanisterialopez.domain.repository.ProductRepository
import com.ebanisterialopez.ebanisterialopez.presentation.model.CategoryProductsIntent
import com.ebanisterialopez.ebanisterialopez.presentation.model.CategoryProductsState
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
private const val CATEGORY_NAME_KEY = "categoryName"

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<CategoryProductsState>(CategoryProductsState.Loading)
    val state: StateFlow<CategoryProductsState> = _state.asStateFlow()

    private val rawCategoryName: String? = savedStateHandle.get<String>(CATEGORY_NAME_KEY)
    val categoryName: String = rawCategoryName?.trim().takeIf { !it.isNullOrBlank() } ?: ""

    init {
        if (categoryName.isNotBlank()) {
            onIntent(CategoryProductsIntent.LoadProducts(categoryName))
        } else {
            _state.value = CategoryProductsState.Error("Categoría no especificada")
        }
    }

    fun onIntent(intent: CategoryProductsIntent) {
        when (intent) {
            is CategoryProductsIntent.LoadProducts -> loadProductsByCategory(intent.category)
        }
    }

    private fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            _state.value = CategoryProductsState.Loading
            try {
                val dtoList = repository.getProductsByCategory(category)
                val domainList: List<Product> = dtoList.map { it.toDomain() }
                _state.value = CategoryProductsState.Success(domainList)
            } catch (e: Exception) {
                _state.value = CategoryProductsState.Error(
                    "Error al cargar productos: ${e.localizedMessage ?: "Desconocido"}"
                )
            }
        }
    }
}