package com.example.shop.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.repositories.CartRepository
import com.example.shop.repositories.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    val cartItems = cartRepository.cartItems

    fun removeProduct(productId: String) {
        cartRepository.removeProductFromCart(productId)
    }

    fun placeOrder() {
        viewModelScope.launch {
            val items = cartItems.value
            if (items.isNotEmpty()) {
                val total = items.sumOf { it.product.price * it.quantity }
                orderRepository.placeOrder(items, total)
            }
        }
    }
}
