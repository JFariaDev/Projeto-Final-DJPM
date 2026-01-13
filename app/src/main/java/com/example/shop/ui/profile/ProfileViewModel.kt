package com.example.shop.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.models.Order
import com.example.shop.repositories.OrderRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    val auth: FirebaseAuth
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        auth.currentUser?.uid?.let { userId ->
            loadOrderHistory(userId)
        }
    }

    private fun loadOrderHistory(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            _orders.value = orderRepository.getOrderHistory(userId)
            _loading.value = false
        }
    }

    fun logout() {
        auth.signOut()
    }
}
