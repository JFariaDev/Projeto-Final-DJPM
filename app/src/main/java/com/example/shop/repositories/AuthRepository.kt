package com.example.shop.repositories

import com.example.shop.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Login error")
                }
            }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onError(task.exception?.message ?: "Register error")
                    return@addOnCompleteListener
                }

                val uid = auth.currentUser?.uid
                if (uid == null) {
                    onError("User id not found")
                    return@addOnCompleteListener
                }

                val user = User(
                    docId = uid,
                    name = name,
                    email = email,
                    bio = ""
                )

                firestore.collection("users")
                    .document(uid)
                    .set(user)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener {
                        onError(it.message ?: "Firestore error")
                    }
            }
    }
}
