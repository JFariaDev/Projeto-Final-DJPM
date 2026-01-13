package com.example.shop.repositories

import com.example.shop.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.java

@Singleton
class ProfileRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun loadUser(
        onResult: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = getCurrentUserId()
        if (uid == null) {
            onError("User not logged in")
            return
        }

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val user = doc?.toObject(User::class.java)
                if (user != null) {
                    onResult(user)
                } else {
                    onError("User data not found")
                }
            }
            .addOnFailureListener {
                onError(it.message ?: "Error loading user")
            }
    }

    fun saveUser(
        user: User,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = getCurrentUserId()
        if (uid == null) {
            onError("User not logged in")
            return
        }

        firestore.collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Error saving user") }
    }
}
