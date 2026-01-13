package com.example.shop.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.models.Product
import com.example.shop.repositories.CartRepository
import com.example.shop.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val productList = productRepository.fetchProducts()
                val product = productList.find { it.id.toString() == productId }

                _product.value = product
            } catch (e: Exception) {
                _product.value = null
            } finally {
                _loading.value = false
            }
        }
    }


    fun addToCart(quantity: Int) {
        val productToAdd = _product.value
        if (productToAdd != null) {
            cartRepository.addProductToCart(productToAdd, quantity)
        }
    }
}