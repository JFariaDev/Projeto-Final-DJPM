// kotlin
package com.example.shop.ui.components

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomBar(
    currentTab: String,
    onTabSelected: (String) -> Unit,
    cartItemCount: Int,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = currentTab == "home",
            onClick = { onTabSelected("home") },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentTab == "cart",
            onClick = { onTabSelected("cart") },
            icon = {
                BadgedBox(badge = {
                    if (cartItemCount > 0) {
                        Badge { Text(cartItemCount.toString()) }
                    }
                }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                }
            },
            label = { Text("Cart") }
        )
        NavigationBarItem(
            selected = currentTab == "profile",
            onClick = { onTabSelected("profile") },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}
