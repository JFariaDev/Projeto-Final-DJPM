package com.example.shop.models

 data class Product(
     val id: Int,
     val title: String,
     val description: String,
     val price: Double,
     val thumbnail: String,
     val category: String,
     val rating: Double,
     val images: List<String> = emptyList()
 )

 data class ProductsResponse(
     val products: List<Product>
 )