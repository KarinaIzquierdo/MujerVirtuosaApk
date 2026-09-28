package com.mujer_virtuosa.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mujer_virtuosa.ui.screens.admin.AdminCatalogScreen
import com.mujer_virtuosa.ui.screens.admin.AdminHomeScreen
import com.mujer_virtuosa.ui.screens.admin.AdminOrdersScreen
import com.mujer_virtuosa.ui.screens.admin.AdminProductFormScreen
import com.mujer_virtuosa.ui.screens.admin.AdminProfileScreen
import com.mujer_virtuosa.ui.screens.admin.AdminProgressScreen
import com.mujer_virtuosa.ui.screens.admin.AdminSalesScreen
import com.mujer_virtuosa.ui.screens.admin.AdminUsersScreen
import com.mujer_virtuosa.ui.screens.user.CartScreen
import com.mujer_virtuosa.ui.screens.user.CatalogScreen
import com.mujer_virtuosa.ui.screens.user.OrdersScreen
import com.mujer_virtuosa.ui.screens.user.ProgressScreen
import com.mujer_virtuosa.ui.screens.user.ProfileScreen
import com.mujer_virtuosa.ui.screens.auth.LoginScreen
import com.mujer_virtuosa.ui.screens.auth.RegisterScreen
import com.mujer_virtuosa.ui.screens.auth.WelcomeScreen
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel
import com.mujer_virtuosa.ui.viewmodel.product.ProductViewModel
import com.mujer_virtuosa.utils.TokenManager

object AppRoutes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val CATALOG = "catalog"
    const val CART = "cart"
    const val ORDERS = "orders"
    const val PROGRESS = "progress"
    const val PROFILE = "profile"
    const val ADMIN_HOME = "admin_home"
    const val ADMIN_CATALOG = "admin_catalog"
    const val ADMIN_PRODUCT_FORM = "admin_product_form"
    const val ADMIN_SALES = "admin_sales"
    const val ADMIN_ORDERS = "admin_orders"
    const val ADMIN_USERS = "admin_users"
    const val ADMIN_PROFILE = "admin_profile"
    const val ADMIN_PROGRESS = "admin_progress"
}

// Destino según el rol del usuario autenticado
private fun homeRoute(): String {
    return if (TokenManager.getUserRole() == "admin") {
        AppRoutes.ADMIN_HOME
    } else {
        AppRoutes.CATALOG
    }
}

private fun NavHostController.navigateToHome() {
    navigate(homeRoute()) {
        popUpTo(AppRoutes.WELCOME) { inclusive = true }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.WELCOME
    ) {
        composable(AppRoutes.WELCOME) {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate(AppRoutes.LOGIN) },
                onNavigateToRegister = { navController.navigate(AppRoutes.REGISTER) },
                onNavigateToCatalog = { navController.navigate(AppRoutes.LOGIN) }
            )
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigateToHome() },
                onNavigateToRegister = { navController.navigate(AppRoutes.REGISTER) },
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigateToHome() },
                onNavigateToLogin = { navController.navigate(AppRoutes.LOGIN) },
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.CATALOG) {
            CatalogScreen(
                onLogout = {
                    navController.navigate(AppRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToCart = { navController.navigate(AppRoutes.CART) },
                onNavigateToOrders = { navController.navigate(AppRoutes.ORDERS) },
                onNavigateToProgress = { navController.navigate(AppRoutes.PROGRESS) },
                onNavigateToProfile = { navController.navigate(AppRoutes.PROFILE) },
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.CART) {
            CartScreen(
                onLogout = {
                    navController.navigate(AppRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToCatalog = { navController.popBackStack() },
                onNavigateToOrders = { navController.navigate(AppRoutes.ORDERS) },
                onNavigateToProgress = { navController.navigate(AppRoutes.PROGRESS) },
                onNavigateToProfile = { navController.navigate(AppRoutes.PROFILE) },
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.ORDERS) {
            OrdersScreen(
                onLogout = {
                    navController.navigate(AppRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToCatalog = {
                    navController.navigate(AppRoutes.CATALOG) {
                        popUpTo(AppRoutes.CATALOG) { inclusive = true }
                    }
                },
                onNavigateToCart = { navController.navigate(AppRoutes.CART) },
                onNavigateToProgress = { navController.navigate(AppRoutes.PROGRESS) },
                onNavigateToProfile = { navController.navigate(AppRoutes.PROFILE) },
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.PROGRESS) {
            ProgressScreen(
                onLogout = {
                    navController.navigate(AppRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToCatalog = {
                    navController.navigate(AppRoutes.CATALOG) {
                        popUpTo(AppRoutes.CATALOG) { inclusive = true }
                    }
                },
                onNavigateToCart = { navController.navigate(AppRoutes.CART) },
                onNavigateToOrders = { navController.navigate(AppRoutes.ORDERS) },
                onNavigateToProfile = { navController.navigate(AppRoutes.PROFILE) },
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.PROFILE) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(AppRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToCatalog = {
                    navController.navigate(AppRoutes.CATALOG) {
                        popUpTo(AppRoutes.CATALOG) { inclusive = true }
                    }
                },
                onNavigateToCart = { navController.navigate(AppRoutes.CART) },
                onNavigateToOrders = { navController.navigate(AppRoutes.ORDERS) },
                onNavigateToProgress = { navController.navigate(AppRoutes.PROGRESS) },
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.ADMIN_HOME) {
            AdminHomeScreen(
                onLogout = {
                    navController.navigate(AppRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToCatalog = { navController.navigate(AppRoutes.ADMIN_CATALOG) },
                onNavigateToSales = { navController.navigate(AppRoutes.ADMIN_SALES) },
                onNavigateToUsers = { navController.navigate(AppRoutes.ADMIN_USERS) },
                onNavigateToProfile = { navController.navigate(AppRoutes.ADMIN_PROFILE) },
                onNavigateToProgress = { navController.navigate(AppRoutes.ADMIN_PROGRESS) },
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.ADMIN_CATALOG) {
            AdminCatalogScreen(
                onLogout = {
                    navController.navigate(AppRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToHome = { navController.popBackStack() },
                onNavigateToSales = {
                    navController.navigate(AppRoutes.ADMIN_SALES) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToUsers = {
                    navController.navigate(AppRoutes.ADMIN_USERS) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(AppRoutes.ADMIN_PROFILE) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToProgress = {
                    navController.navigate(AppRoutes.ADMIN_PROGRESS) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onAddProduct = {
                    productViewModel.selectProduct(null)
                    navController.navigate(AppRoutes.ADMIN_PRODUCT_FORM)
                },
                onEditProduct = { product ->
                    productViewModel.selectProduct(product)
                    navController.navigate(AppRoutes.ADMIN_PRODUCT_FORM)
                },
                productViewModel = productViewModel,
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.ADMIN_SALES) {
            AdminSalesScreen(
                onLogout = {
                    navController.navigate(AppRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToHome = { navController.popBackStack() },
                onNavigateToCatalog = {
                    navController.navigate(AppRoutes.ADMIN_CATALOG) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onViewAllOrders = { navController.navigate(AppRoutes.ADMIN_ORDERS) },
                onNavigateToUsers = {
                    navController.navigate(AppRoutes.ADMIN_USERS) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(AppRoutes.ADMIN_PROFILE) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToProgress = {
                    navController.navigate(AppRoutes.ADMIN_PROGRESS) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.ADMIN_USERS) {
            AdminUsersScreen(
                onLogout = {
                    navController.navigate(AppRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToHome = { navController.popBackStack() },
                onNavigateToCatalog = {
                    navController.navigate(AppRoutes.ADMIN_CATALOG) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToSales = {
                    navController.navigate(AppRoutes.ADMIN_SALES) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(AppRoutes.ADMIN_PROFILE) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToProgress = {
                    navController.navigate(AppRoutes.ADMIN_PROGRESS) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.ADMIN_PROFILE) {
            AdminProfileScreen(
                onLogout = {
                    navController.navigate(AppRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToHome = { navController.popBackStack() },
                onNavigateToCatalog = {
                    navController.navigate(AppRoutes.ADMIN_CATALOG) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToSales = {
                    navController.navigate(AppRoutes.ADMIN_SALES) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToUsers = {
                    navController.navigate(AppRoutes.ADMIN_USERS) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToProgress = {
                    navController.navigate(AppRoutes.ADMIN_PROGRESS) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onViewOrders = { navController.navigate(AppRoutes.ADMIN_ORDERS) },
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.ADMIN_PROGRESS) {
            AdminProgressScreen(
                onLogout = {
                    navController.navigate(AppRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToHome = { navController.popBackStack() },
                onNavigateToCatalog = {
                    navController.navigate(AppRoutes.ADMIN_CATALOG) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToSales = {
                    navController.navigate(AppRoutes.ADMIN_SALES) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToUsers = {
                    navController.navigate(AppRoutes.ADMIN_USERS) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(AppRoutes.ADMIN_PROFILE) {
                        popUpTo(AppRoutes.ADMIN_HOME)
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.ADMIN_ORDERS) {
            AdminOrdersScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.ADMIN_PRODUCT_FORM) {
            AdminProductFormScreen(
                onBack = { navController.popBackStack() },
                productViewModel = productViewModel
            )
        }
    }
}
