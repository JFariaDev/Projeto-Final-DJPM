package com.example.shop.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val items: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    @ServerTimestamp
    val timestamp: Date? = null
)
