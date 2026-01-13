// app/src/main/java/com/example/shop/ui/ProductListScreen.kt
package com.example.shop.ui.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.shop.models.Product


@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onProductClick: (String) -> Unit
) {
    val products by viewModel.visibleProducts.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val productsByCategory = products.groupBy { it.category }

    Box(modifier = Modifier.fillMaxSize()) {

        if (loading && products.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            return@Box
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Surface(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Selected: ${selectedCategory ?: "(all)"}")
                    Text(text = "Visible: ${products.size}")
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                if (selectedCategory == null) {
                    productsByCategory.forEach { (category, items) ->
                        item {
                            CategorySection(
                                category = category,
                                products = items,
                                onProductClick = onProductClick
                            )
                        }
                    }
                } else {
                    val itemsForCategory = productsByCategory[selectedCategory] ?: emptyList()
                    if (itemsForCategory.isNotEmpty()) {
                        item {
                            Text(
                                text = selectedCategory!!.uppercase(),
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        items(itemsForCategory) { product ->
                            LargeProductCard(product, onProductClick = { onProductClick(product.id.toString()) })
                        }
                    } else {
                    }
                }
            }
        }
    }
}


@Composable
fun CategorySection(
    category: String,
    products: List<Product>,
    onProductClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text(
            text = category.uppercase(),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 4.dp)
        )

        products.forEach { product ->
            LargeProductCard(product, onProductClick = { onProductClick(product.id.toString()) })
        }
    }
}

@Composable
fun LargeProductCard(
    product: Product,
    onProductClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable(onClick = onProductClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            AsyncImage(
                model = product.thumbnail,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "★ ${product.rating}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "${product.price}€",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
