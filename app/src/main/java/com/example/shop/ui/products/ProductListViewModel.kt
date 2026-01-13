package com.example.shop.ui.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.models.Product
import com.example.shop.repositories.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repo: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _categories = MutableStateFlow<List<String>?>(null)
    val categories: StateFlow<List<String>?> = _categories

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val _visibleProducts = MutableStateFlow<List<Product>>(emptyList())
    val visibleProducts: StateFlow<List<Product>> = _visibleProducts

    init {
        viewModelScope.launch {
            combine(_products, _selectedCategory) { products, sel ->
                if (sel.isNullOrEmpty()) products
                else products.filter { it.category == sel }
            }.collect { filtered ->
                _visibleProducts.value = filtered
                Log.d("ProductListVM", "visibleProducts updated: size=${filtered.size} (selected=${_selectedCategory.value})")
            }
        }

        load()
    }

    fun setSelectedCategory(category: String?) {
        _selectedCategory.value = category
        Log.d("ProductListVM", "setSelectedCategory -> $category")
    }

    fun load() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val fetched = repo.fetchProducts()
                _products.value = fetched

                val cats = fetched.map { it.category }
                    .distinct()
                    .sorted()
                _categories.value = cats

                Log.d("ProductListVM", "load success: products=${fetched.size}, categories=${cats.size}")

            } catch (ex: Exception) {
                Log.e("ProductListVM", "load failed", ex)
                _products.value = emptyList()
                _categories.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }
}