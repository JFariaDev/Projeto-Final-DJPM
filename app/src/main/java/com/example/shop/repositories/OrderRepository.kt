package com.example.shop.repositories

import android.util.Log
import com.example.shop.models.CartItem
import com.example.shop.models.Order
import com.example.shop.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val cartRepository: CartRepository
) {

    suspend fun placeOrder(cartItems: List<CartItem>, totalPrice: Double) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val orderId = firestore.collection("orders").document().id
            val order = Order(
                orderId = orderId,
                userId = currentUser.uid,
                items = cartItems,
                totalPrice = totalPrice
            )
            firestore.collection("orders").document(orderId).set(order).await()
            cartRepository.clearCart()
        } else {
            throw IllegalStateException("User not logged in")
        }
    }

    suspend fun getOrderHistory(userId: String): List<Order> {
        return try {
            val snapshot = firestore.collection("orders")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                // safer handling: treat items as List<*> and item/product as Map<*, *>
                val itemsRaw = document.get("items") as? List<*> ?: emptyList<Any>()
                val cartItems = itemsRaw.mapNotNull { itemAny ->
                    val itemMap = itemAny as? Map<*, *> ?: return@mapNotNull null
                    val productMap = itemMap["product"] as? Map<*, *> ?: return@mapNotNull null

                    // parse quantity safely
                    val quantity = when (val q = itemMap["quantity"]) {
                        is Number -> q.toInt()
                        is String -> q.toIntOrNull() ?: 0
                        else -> 0
                    }

                    val id = when (val v = productMap["id"]) {
                        is Number -> v.toInt()
                        is String -> v.toIntOrNull() ?: 0
                        else -> 0
                    }
                    val title = productMap["title"] as? String ?: ""
                    val description = productMap["description"] as? String ?: ""
                    val price = when (val p = productMap["price"]) {
                        is Number -> p.toDouble()
                        is String -> p.toDoubleOrNull() ?: 0.0
                        else -> 0.0
                    }
                    val thumbnail = productMap["thumbnail"] as? String ?: ""
                    val category = productMap["category"] as? String ?: ""
                    val rating = when (val r = productMap["rating"]) {
                        is Number -> r.toDouble()
                        is String -> r.toDoubleOrNull() ?: 0.0
                        else -> 0.0
                    }
                    val images = (productMap["images"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

                    val product = Product(
                        id = id,
                        title = title,
                        description = description,
                        price = price,
                        thumbnail = thumbnail,
                        category = category,
                        rating = rating,
                        images = images
                    )

                    CartItem(product = product, quantity = quantity)
                }

                val orderId = document.getString("orderId") ?: document.id
                val uid = document.getString("userId") ?: ""
                val totalPrice = document.getDouble("totalPrice") ?: document.getDouble("total") ?: 0.0
                val timestamp = document.getTimestamp("timestamp")?.toDate()

                Order(
                    orderId = orderId,
                    userId = uid,
                    items = cartItems,
                    totalPrice = totalPrice,
                    timestamp = timestamp
                )
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error fetching order history", e)
            emptyList()
        }
    }
}
