package com.example.shop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shop.ui.cart.CartScreen
import com.example.shop.ui.cart.CartViewModel
import com.example.shop.ui.components.MyBottomBar
import com.example.shop.ui.components.MyTopBar
import com.example.shop.ui.login.LoginView
import com.example.shop.ui.products.ProductDetailScreen
import com.example.shop.ui.products.ProductListScreen
import com.example.shop.ui.products.ProductListViewModel
import com.example.shop.ui.profile.ProfileView
import com.example.shop.ui.register.RegisterView
import com.example.shop.ui.theme.ShopTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: ""

            val productViewModel: ProductListViewModel = hiltViewModel()
            val cartViewModel: CartViewModel = hiltViewModel()
            val cartItems by cartViewModel.cartItems.collectAsState()

            val categories by productViewModel.categories.collectAsState()
            val isProductLoading by productViewModel.loading.collectAsState()
            val selectedCategory by productViewModel.selectedCategory.collectAsState()

            var isLoggedIn by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser != null) }

            LaunchedEffect(Unit) {
                FirebaseAuth.getInstance().addAuthStateListener { auth ->
                    isLoggedIn = auth.currentUser != null
                }
            }

            ShopTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTER) {
                            MyTopBar(
                                categories = categories ?: emptyList(),
                                loading = isProductLoading,
                                currentCategory = selectedCategory,
                                onCategorySelected = { productViewModel.setSelectedCategory(it) },
                                topBarTitle = if (currentRoute == Routes.PRODUCTS) "Shop" else "Profile",
                                isHomeScreen = currentRoute == Routes.PRODUCTS
                            )
                        }
                    },
                    bottomBar = {
                        if (currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTER) {
                            MyBottomBar(
                                currentTab = when {
                                    currentRoute?.startsWith("product") == true -> "home"
                                    currentRoute == Routes.PROFILE -> "profile"
                                    currentRoute == Routes.CART -> "cart"
                                    else -> "home"
                                },
                                onTabSelected = { tab ->
                                    when (tab) {
                                        "home" -> navController.navigate(Routes.PRODUCTS)
                                        "profile" -> navController.navigate(Routes.PROFILE)
                                        "cart" -> navController.navigate(Routes.CART)
                                    }
                                },
                                cartItemCount = cartItems.sumOf { it.quantity }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (isLoggedIn) Routes.PRODUCTS else Routes.LOGIN,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Routes.LOGIN) { LoginView(navController = navController) }
                        composable(Routes.REGISTER) { RegisterView(navController = navController) }
                        composable(Routes.PRODUCTS) {
                            ProductListScreen(
                                viewModel = productViewModel,
                                onProductClick = { productId ->
                                    navController.navigate(Routes.productDetail(productId))
                                }
                            )
                        }
                        composable(Routes.PROFILE) { ProfileView(navController = navController) }
                        composable(Routes.CART) { CartScreen(navController = navController) }
                        composable(
                            route = Routes.PRODUCT_DETAIL,
                            arguments = listOf(navArgument("productId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getString("productId")
                            if (productId != null) {
                                ProductDetailScreen(
                                    productId = productId,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PRODUCTS = "products"
    const val PROFILE = "profile"
    const val CART = "cart"
    const val PRODUCT_DETAIL = "product_detail/{productId}"

    fun productDetail(productId: String) = "product_detail/$productId"
}
