package com.example.shop.network

import com.example.shop.models.ProductsResponse
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): ProductsResponse
}