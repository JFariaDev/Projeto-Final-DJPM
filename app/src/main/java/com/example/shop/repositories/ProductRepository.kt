package com.example.shop.repositories

import com.example.shop.models.Product
import com.example.shop.network.NetworkModule
import javax.inject.Inject // Adicione este import

class ProductRepository @Inject constructor() {
    private val api = NetworkModule.api
    suspend fun fetchProducts(): List<Product> = api.getProducts().products
}