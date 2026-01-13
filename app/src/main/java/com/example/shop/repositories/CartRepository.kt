package com.example.shop.repositories

import com.example.shop.models.CartItem
import com.example.shop.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun addProductToCart(product: Product, quantity: Int) {
        val currentCart = _cartItems.value.toMutableList()
        val existingItem = currentCart.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            currentCart.add(CartItem(product = product, quantity = quantity))
        }
        _cartItems.value = currentCart
    }

    fun removeProductFromCart(productId: String) {
        val currentCart = _cartItems.value.toMutableList()
        currentCart.removeAll { it.product.id.toString() == productId }
        _cartItems.value = currentCart
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
